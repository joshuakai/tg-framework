package com.tg.framework.commons.concurrent.task.redis;

import com.tg.framework.commons.concurrent.task.MutexTask;
import com.tg.framework.commons.concurrent.task.MutexTaskContext;
import com.tg.framework.commons.concurrent.task.MutexTaskJob;
import com.tg.framework.commons.concurrent.task.MutexTaskJobResult;
import com.tg.framework.commons.concurrent.task.MutexTaskJobStatus;
import com.tg.framework.commons.concurrent.task.MutexTaskService;
import com.tg.framework.commons.concurrent.task.exception.TaskMutexException;
import com.tg.framework.commons.concurrent.task.support.SimpleMutexTaskBuilder;
import com.tg.framework.commons.concurrent.task.support.SimpleMutexTaskJobResult;
import com.tg.framework.commons.concurrent.task.support.SimpleMutexTaskJobResultBuilder;
import com.tg.framework.commons.util.OptionalUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

public class RedisMutexTaskService implements MutexTaskService {

  private static Logger logger = LoggerFactory.getLogger(RedisMutexTaskService.class);

  private static final long ENDLESS_TIMEOUT_MILLIS = -1L;
  private static final String DEFAULT_KEY_PREFIX = "mutex_tasks:";
  private static final String CURRENT_SUFFIX = ":current";
  private static final String CURRENT_STARTED_SUFFIX = ":started";
  private static final String FINISHED_STEPS_SUFFIX = ":finished_steps";
  private static final String RESULTS_SUFFIX = ":results";
  private static final String PREVIOUS_SUFFIX = ":previous";
  private static final String KEY_SEPARATOR = ":";
  private static final String RESULTS_SUFFIX_LIKE = ":results*";
  private static final String FINISHED_STEPS_SUFFIX_LIKE = ":finished_steps*";

  private ExecutorService executorService;
  private RedisTemplate redisTemplate;
  private String keyPrefix;

  public RedisMutexTaskService(RedisTemplate redisTemplate, ExecutorService executorService) {
    this(redisTemplate, DEFAULT_KEY_PREFIX, executorService);
  }

  public RedisMutexTaskService(RedisTemplate redisTemplate, String keyPrefix,
      ExecutorService executorService) {
    Assert.notNull(redisTemplate, "RedisTemplate must not be null.");
    Assert.isTrue(StringUtils.isNotBlank(keyPrefix), "keyPrefix must not be blank.");
    Assert.notNull(executorService, "ExecutorService must not be null.");
    this.redisTemplate = redisTemplate;
    this.keyPrefix = keyPrefix;
    this.executorService = executorService;
  }

  @SuppressWarnings({"unchecked", "ConstantConditions"})
  @Override
  public Long tryStarting(String key, String title, String startedBy, long historyKeepMillis)
      throws TaskMutexException {
    Long lock = RandomUtils.nextLong();
    RedisMutexTask task = new RedisMutexTask(key, lock, title, startedBy, historyKeepMillis);
    if (BooleanUtils
        .isNotTrue(redisTemplate.opsForValue().setIfAbsent(formatKey(key, CURRENT_SUFFIX), task))) {
      throw new TaskMutexException(key, "Task exists.");
    }
    redisTemplate.delete(
        Stream
            .of(
                formatKey(key, CURRENT_STARTED_SUFFIX), formatKey(key, RESULTS_SUFFIX_LIKE),
                formatKey(key, FINISHED_STEPS_SUFFIX_LIKE)
            )
            .map(pattern -> redisTemplate.keys(pattern))
            .flatMap(Set::stream)
            .collect(Collectors.toSet())
    );
    logger.debug("Start task '{}' {} {}.", key, lock, task);
    return lock;
  }

  @SuppressWarnings("unchecked")
  @Override
  public MutexTask get(String key) {
    RedisMutexTask task = (RedisMutexTask) redisTemplate.opsForValue()
        .get(formatKey(key, CURRENT_SUFFIX));
    if (task != null) {
      boolean started = Optional.ofNullable(
          redisTemplate.opsForValue().get(formatKey(key, CURRENT_STARTED_SUFFIX))
      ).map(v -> (boolean) v).orElse(false);
      int finishedSteps = Optional.ofNullable(
          redisTemplate.opsForValue()
              .get(concat(formatKey(key, FINISHED_STEPS_SUFFIX), task.getLock()))
      ).map(v -> (int) v).orElse(0);
      String resultsKey = concat(formatKey(key, RESULTS_SUFFIX), task.getLock());
      List<SimpleMutexTaskJobResult> results = Optional
          .ofNullable(redisTemplate.opsForList().range(resultsKey, 0, -1))
          .orElse(new ArrayList<>(0));
      results.sort(MutexTaskJobResult::sort);
      return mapMutexTask(task, started, finishedSteps, results);
    } else {
      String previousKey = formatKey(key, PREVIOUS_SUFFIX);
      return (MutexTask) redisTemplate.opsForValue().get(previousKey);
    }
  }

  private static void validateParams(String key, Long lock, MutexTaskContext context)
      throws TaskMutexException {
    if (StringUtils.isBlank(key)) {
      throw new TaskMutexException("key must not be empty.", key);
    }
    if (lock == null) {
      throw new TaskMutexException("lock must not be null.", key);
    }
    if (context == null) {
      throw new TaskMutexException("MutexTaskContext must not be null.", key);
    }
    if (CollectionUtils.isEmpty(context.getMainJobs())) {
      throw new TaskMutexException("MutexTaskContext.mainJobs must not be empty.", key);
    }
  }

  @SuppressWarnings("unchecked")
  private RedisMutexTask ensureTask(String key, Long lock) throws TaskMutexException {
    RedisMutexTask redisMutexTask = (RedisMutexTask) redisTemplate.opsForValue()
        .get(formatKey(key, CURRENT_SUFFIX));
    if (redisMutexTask == null || !Objects.equals(redisMutexTask.getLock(), lock)) {
      throw new TaskMutexException("Task not found.", key);
    }

    if (BooleanUtils.isNotTrue(
        redisTemplate.opsForValue().setIfAbsent(formatKey(key, CURRENT_STARTED_SUFFIX), true))) {
      throw new TaskMutexException("Task started.", key);
    }
    return redisMutexTask;
  }

  @SuppressWarnings("unchecked")
  private MutexTask innerStart(RedisMutexTask redisMutexTask, String key, Long lock,
      MutexTaskContext context) {
    final String currentKey = formatKey(key, CURRENT_SUFFIX);
    final String currentStartedKey = formatKey(key, CURRENT_STARTED_SUFFIX);
    int mainTotalSteps = context.getMainJobs().size();
    redisMutexTask.setTotalSteps(mainTotalSteps);
    redisMutexTask.setStartedAt(LocalDateTime.now());
    redisTemplate.opsForValue().set(currentKey, redisMutexTask);
    String finishedStepsKey = concat(formatKey(key, FINISHED_STEPS_SUFFIX), lock);
    redisTemplate.opsForValue().set(finishedStepsKey, 0);
    CompletableFuture.runAsync(() -> {
      final String resultsKey = concat(formatKey(key, RESULTS_SUFFIX), lock);
      final String previousKey = formatKey(key, PREVIOUS_SUFFIX);
      final RedisMutexTask task = redisMutexTask;
      final List<MutexTaskJob> preparingJobs = context.getPreparingJobs();
      final List<MutexTaskJob> mainJobs = context.getMainJobs();
      final List<MutexTaskJob> finishingJobs = context.getFinishingJobs();
      final List<CompletableFuture> preparingFeatures = new ArrayList<>();
      final List<CompletableFuture> mainFeatures = new ArrayList<>();
      final List<CompletableFuture> finishingFeatures = new ArrayList<>();
      final Callable preparingCanceller = () -> {
        logger.info("Cancel task {} while preparing.", task);
        preparingFeatures.forEach(cf -> cf.cancel(true));
        return null;
      };
      final Callable mainCanceller = () -> {
        logger.info("Cancel task {} while executing main.", task);
        mainFeatures.forEach(cf -> cf.cancel(true));
        return null;
      };
      final Callable finishingCanceller = () -> {
        logger.info("Cancel task {} while finishing.", task);
        finishingFeatures.forEach(cf -> cf.cancel(true));
        return null;
      };
      handleJobs(redisMutexTask, redisTemplate, resultsKey, preparingJobs, preparingFeatures,
          preparingCanceller, executorService, false, finishedStepsKey);

      List<SimpleMutexTaskJobResult> results = redisTemplate.opsForList().range(resultsKey, 0, -1);

      if (OptionalUtils.notEmpty(results).map(List::stream)
          .map(stream -> stream.allMatch(MutexTaskJobResult::isSucceed)).orElse(true)) {
        handleJobs(redisMutexTask, redisTemplate, resultsKey, mainJobs, mainFeatures, mainCanceller,
            executorService, true, finishedStepsKey);
        handleJobs(redisMutexTask, redisTemplate, resultsKey, finishingJobs, finishingFeatures,
            finishingCanceller, executorService, false, finishedStepsKey);
      }
      int totalSteps = Stream.of(preparingFeatures, mainFeatures, finishingFeatures).map(List::size)
          .reduce((i, s) -> i + s).orElse(0);
      finish(redisTemplate, currentKey, totalSteps, currentStartedKey, finishedStepsKey, resultsKey,
          previousKey, lock);
    }, executorService);
    return mapMutexTask(redisMutexTask, true, 0, new ArrayList<>(0));
  }

  @Override
  public MutexTask start(String key, Long lock, MutexTaskContext context)
      throws TaskMutexException {
    validateParams(key, lock, context);
    return innerStart(ensureTask(key, lock), key, lock, context);
  }

  @Override
  public CompletableFuture<MutexTask> startAsync(String key, Long lock, MutexTaskContext context)
      throws TaskMutexException {
    validateParams(key, lock, context);
    final RedisMutexTask redisMutexTask = ensureTask(key, lock);
    return CompletableFuture
        .supplyAsync(() -> innerStart(redisMutexTask, key, lock, context), executorService);
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean forceRelease(String key) {
    return BooleanUtils.isTrue(redisTemplate.delete(formatKey(key, CURRENT_SUFFIX)));
  }

  @SuppressWarnings("unchecked")
  private static void handleJobs(RedisMutexTask task, RedisTemplate redisTemplate,
      String resultsKey, List<MutexTaskJob> jobs, List<CompletableFuture> futures,
      Callable canceller, ExecutorService executorService, boolean isMain,
      String finishedStepsKey) {
    if (CollectionUtils.isEmpty(jobs)) {
      return;
    }
    jobs.stream().map(
        job -> {
          SimpleMutexTaskJobResultBuilder builder = SimpleMutexTaskJobResultBuilder
              .aSimpleMutexTaskJobResult()
              .withTitle(job.getTitle())
              .withStarted(true)
              .withStartedAt(LocalDateTime.now());
          return CompletableFuture
              .runAsync(
                  () -> {
                    MutexTaskJobStatus status = job.getExecutor().apply(canceller);
                    builder.withSucceed(status.isSucceed()).withMessage(status.getMessage());
                    logger.debug("Succeed to execute job {} {}.", task, job);
                  },
                  executorService
              )
              .exceptionally(
                  ex -> {
                    builder.withSucceed(false).withMessage(ex.getMessage());
                    logger.error("Failed to execute job {} {}.", task, job, ex);
                    return null;
                  }
              )
              .thenRun(
                  () -> {
                    redisTemplate.opsForList().rightPush(resultsKey,
                        builder.withStopped(true).withStoppedAt(LocalDateTime.now()).build());
                    if (isMain) {
                      redisTemplate.opsForValue().increment(finishedStepsKey, 1);
                    }
                  }
              );
        }
    ).forEach(futures::add);
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
  }

  @SuppressWarnings("unchecked")
  private void finish(RedisTemplate redisTemplate, String currentKey, int totalSteps,
      String currentStartedKey, String finishedStepsKey, String resultsKey, String previousKey,
      Long lock) {
    RedisMutexTask task = (RedisMutexTask) redisTemplate.opsForValue().get(currentKey);
    if (task == null || !Objects.equals(task.getLock(), lock)) {
      redisTemplate.delete(resultsKey);
      logger.debug(
          "Skip finishing because of task changing which may cause by force release '{}' {}.",
          currentKey, lock);
      return;
    }
    task.setStopped(true);
    task.setStoppedAt(LocalDateTime.now());
    int finishedSteps = Optional.ofNullable(
        redisTemplate.opsForValue().get(finishedStepsKey)
    ).map(v -> (int) v).orElse(0);
    List<SimpleMutexTaskJobResult> results = Optional
        .ofNullable(redisTemplate.opsForList().range(resultsKey, 0, -1)).orElse(new ArrayList<>(0));
    task.setSucceed(results.stream().allMatch(SimpleMutexTaskJobResult::isSucceed)
        && results.size() == totalSteps);
    results.sort(MutexTaskJobResult::sort);

    MutexTask previousTask = mapMutexTask(task, true, finishedSteps, results);

    if (task.getHistoryKeepMillis() == ENDLESS_TIMEOUT_MILLIS) {
      redisTemplate.opsForValue().set(previousKey, previousTask);
    } else {
      redisTemplate.opsForValue()
          .set(previousKey, previousTask, task.getHistoryKeepMillis(), TimeUnit.MILLISECONDS);
    }
    redisTemplate.delete(Stream.of(currentStartedKey, finishedStepsKey, resultsKey, currentKey)
        .collect(Collectors.toSet()));
    logger.debug("Finish task '{}' {} {}.", task.getKey(), lock, task);
  }

  private String formatKey(String key, String suffix) {
    return StringUtils.join(keyPrefix, key, suffix);
  }

  private static String concat(Object... keys) {
    return StringUtils.join(keys, KEY_SEPARATOR);
  }

  private static MutexTask mapMutexTask(RedisMutexTask task, boolean started, int finishedSteps,
      List<SimpleMutexTaskJobResult> results) {
    return SimpleMutexTaskBuilder.aSimpleMutexTask()
        .withKey(task.getKey())
        .withLock(task.getLock())
        .withTitle(task.getTitle())
        .withStartedBy(task.getStartedBy())
        .withStartedAt(task.getStartedAt())
        .withTotalSteps(task.getTotalSteps())
        .withFinishedSteps(finishedSteps)
        .withStarted(started)
        .withStopped(task.isStopped())
        .withSucceed(task.isSucceed())
        .withStoppedAt(task.getStoppedAt())
        .withResults(results)
        .build();
  }

  private static class RedisMutexTask implements Serializable {

    private static final long serialVersionUID = -3917357235008072852L;

    private String key;
    private Long lock;
    private String title;
    private String startedBy;
    private LocalDateTime startedAt;
    private int totalSteps;
    private boolean stopped;
    private boolean succeed;
    private LocalDateTime stoppedAt;
    private long historyKeepMillis;

    public RedisMutexTask() {
    }

    public RedisMutexTask(String key, Long lock, String title, String startedBy,
        long historyKeepMillis) {
      this.key = key;
      this.lock = lock;
      this.title = title;
      this.startedBy = startedBy;
      this.historyKeepMillis = historyKeepMillis;
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public Long getLock() {
      return lock;
    }

    public void setLock(Long lock) {
      this.lock = lock;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getStartedBy() {
      return startedBy;
    }

    public void setStartedBy(String startedBy) {
      this.startedBy = startedBy;
    }

    public LocalDateTime getStartedAt() {
      return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
      this.startedAt = startedAt;
    }

    public int getTotalSteps() {
      return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
      this.totalSteps = totalSteps;
    }

    public boolean isStopped() {
      return stopped;
    }

    public void setStopped(boolean stopped) {
      this.stopped = stopped;
    }

    public boolean isSucceed() {
      return succeed;
    }

    public void setSucceed(boolean succeed) {
      this.succeed = succeed;
    }

    public LocalDateTime getStoppedAt() {
      return stoppedAt;
    }

    public void setStoppedAt(LocalDateTime stoppedAt) {
      this.stoppedAt = stoppedAt;
    }

    public long getHistoryKeepMillis() {
      return historyKeepMillis;
    }

    public void setHistoryKeepMillis(long historyKeepMillis) {
      this.historyKeepMillis = historyKeepMillis;
    }

    @Override
    public String toString() {
      return "RedisMutexTask{" +
          "key='" + key + '\'' +
          ", lock=" + lock +
          ", title='" + title + '\'' +
          ", startedBy='" + startedBy + '\'' +
          ", startedAt=" + startedAt +
          ", totalSteps=" + totalSteps +
          ", stopped=" + stopped +
          ", succeed=" + succeed +
          ", stoppedAt=" + stoppedAt +
          ", historyKeepMillis=" + historyKeepMillis +
          '}';
    }
  }
}
