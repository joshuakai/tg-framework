package com.tg.framework.commons.concurrent.task.standalone;

import com.tg.framework.commons.concurrent.pool.DaemonExecutorService;
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
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class StandaloneMutexTaskService implements MutexTaskService {

  private static final long ENDLESS_TIMEOUT_MILLIS = -1L;
  private static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 5000L;

  private static Logger logger = LoggerFactory.getLogger(StandaloneMutexTaskService.class);

  private final ConcurrentHashMap<String, ConcurrentMutexTask> taskHolder = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, ConcurrentMutexTask> previousTaskHolder = new ConcurrentHashMap<>();

  private ExecutorService executorService;
  private DaemonExecutorService daemonExecutorService;
  private long timeBetweenEvictionRunsMillis;

  public StandaloneMutexTaskService(ExecutorService executorService,
      DaemonExecutorService daemonExecutorService) {
    this(executorService, daemonExecutorService, DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
  }

  public StandaloneMutexTaskService(ExecutorService executorService,
      DaemonExecutorService daemonExecutorService, long timeBetweenEvictionRunsMillis) {
    Assert.notNull(executorService, "executorService must not be null.");
    Assert.notNull(daemonExecutorService, "daemonExecutorService must not be null.");
    this.executorService = executorService;
    this.daemonExecutorService = daemonExecutorService;
    this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    startExpireThread();
  }

  private void startExpireThread() {
    daemonExecutorService.execute(() -> {
      while (true) {
        logger.debug("Check expired Task.");
        Iterator<Entry<String, ConcurrentMutexTask>> ite = previousTaskHolder.entrySet()
            .iterator();
        while (ite.hasNext()) {
          Optional.ofNullable(ite.next().getValue())
              .filter(StandaloneMutexTaskService::isExpired)
              .ifPresent(ut -> {
                logger.debug("Expired Task found {}.", ut);
                ite.remove();
              });
        }
        try {
          Thread.sleep(timeBetweenEvictionRunsMillis);
        } catch (InterruptedException e) {
          logger.error("Expire thread was interrupted.", e);
          break;
        }
      }
    });
  }

  @Override
  public Long tryStarting(String key, String title, String startedBy, long historyKeepMillis)
      throws TaskMutexException {
    Long lock = RandomUtils.nextLong();
    ConcurrentMutexTask task = new ConcurrentMutexTask(key, lock, title, startedBy,
        historyKeepMillis);
    ConcurrentMutexTask previous;
    if ((previous = taskHolder.putIfAbsent(key, task)) != null) {
      throw new TaskMutexException(key, String.format("Task exists %d.", previous.getLock()));
    }
    logger.debug("Start task '{}' {} {}.", key, lock, task);
    return lock;
  }

  @Override
  public MutexTask get(String key) {
    ConcurrentMutexTask task = taskHolder.get(key);
    if (task != null) {
      task.getResults().sort(MutexTaskJobResult::sort);
    } else {
      task = previousTaskHolder.get(key);
    }
    return Optional.ofNullable(task).map(StandaloneMutexTaskService::mapMutexTask).orElse(null);
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

  private ConcurrentMutexTask ensureTask(String key, Long lock) throws TaskMutexException {
    ConcurrentMutexTask concurrentMutexTask = taskHolder.get(key);
    if (concurrentMutexTask == null || !Objects.equals(concurrentMutexTask.getLock(), lock)) {
      throw new TaskMutexException("Task not found.", key);
    }
    if (concurrentMutexTask.getStarted().compareAndSet(false, true)) {
      throw new TaskMutexException("Task started.", key);
    }
    return concurrentMutexTask;
  }

  private MutexTask innerStart(ConcurrentMutexTask concurrentMutexTask, String key, Long lock,
      MutexTaskContext context) {
    int mainTotalSteps = context.getMainJobs().size();
    concurrentMutexTask.setTotalSteps(mainTotalSteps);
    concurrentMutexTask.setStartedAt(LocalDateTime.now());
    concurrentMutexTask
        .setResults(Collections.synchronizedList(new ArrayList<>(mainTotalSteps + 1)));
    CompletableFuture.runAsync(() -> {
      final ConcurrentMutexTask task = concurrentMutexTask;
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
      handleJobs(task, preparingJobs, preparingFeatures, preparingCanceller, executorService,
          false);
      if (task.getResults().stream().allMatch(MutexTaskJobResult::isSucceed)) {
        handleJobs(task, mainJobs, mainFeatures, mainCanceller, executorService, true);
        handleJobs(task, finishingJobs, finishingFeatures, finishingCanceller, executorService,
            false);
      }
      int totalSteps = Stream.of(preparingFeatures, mainFeatures, finishingFeatures).map(List::size)
          .reduce((i, s) -> i + s).orElse(0);
      finish(key, lock, totalSteps);
    }, executorService);
    return mapMutexTask(concurrentMutexTask);
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
    final ConcurrentMutexTask concurrentMutexTask = ensureTask(key, lock);
    return CompletableFuture
        .supplyAsync(() -> innerStart(concurrentMutexTask, key, lock, context), executorService);
  }

  @Override
  public boolean forceRelease(String key) {
    return taskHolder.remove(key) != null;
  }

  private static void handleJobs(ConcurrentMutexTask task, List<MutexTaskJob> jobs,
      List<CompletableFuture> futures, Callable canceller, ExecutorService executorService,
      boolean isMainJob) {
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
                    MutexTaskJobStatus status = job.getExecutor().execute(canceller);
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
                    task.getResults()
                        .add(builder.withStopped(true).withStoppedAt(LocalDateTime.now()).build());
                    if (isMainJob) {
                      task.getFinishedSteps().incrementAndGet();
                    }
                  }
              );
        }
    ).forEach(futures::add);
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
  }

  private void finish(String key, Long lock, int totalSteps) {
    ConcurrentMutexTask task = taskHolder.get(key);
    if (task == null || !Objects.equals(task.getLock(), lock)) {
      logger.debug(
          "Skip finishing because of task changing which may cause by force release '{}' {}.", key,
          lock);
      return;
    }
    task.setStopped(true);
    task.setStoppedAt(LocalDateTime.now());
    task.setSucceed(task.getResults().stream().allMatch(SimpleMutexTaskJobResult::isSucceed)
        && task.getResults().size() == totalSteps);
    long historyKeepMillis = task.getHistoryKeepMillis();
    task.setHistoryExpiredAt(historyKeepMillis == ENDLESS_TIMEOUT_MILLIS ? historyKeepMillis
        : System.currentTimeMillis() + historyKeepMillis);
    task.getResults().sort(MutexTaskJobResult::sort);
    previousTaskHolder.put(key, task);
    taskHolder.remove(key);
    logger.debug("Finish task '{}' {} {}.", key, lock, task);
  }

  private static MutexTask mapMutexTask(ConcurrentMutexTask task) {
    return SimpleMutexTaskBuilder.aSimpleMutexTask()
        .withKey(task.getKey())
        .withLock(task.getLock())
        .withTitle(task.getTitle())
        .withStartedBy(task.getStartedBy())
        .withStartedAt(task.getStartedAt())
        .withTotalSteps(task.getTotalSteps())
        .withFinishedSteps(task.getFinishedSteps().get())
        .withStarted(task.getStarted().get())
        .withStopped(task.isStopped())
        .withSucceed(task.isSucceed())
        .withStoppedAt(task.getStoppedAt())
        .withResults(task.getResults())
        .build();
  }

  private static boolean isExpired(ConcurrentMutexTask uniqueTask) {
    return uniqueTask.getHistoryExpiredAt() != ENDLESS_TIMEOUT_MILLIS
        && System.currentTimeMillis() >= uniqueTask.getHistoryExpiredAt();
  }

  private static class ConcurrentMutexTask implements Serializable {

    private static final long serialVersionUID = -3917357235008072852L;

    private String key;
    private Long lock;
    private String title;
    private String startedBy;
    private LocalDateTime startedAt;
    private int totalSteps;
    private AtomicInteger finishedSteps = new AtomicInteger(0);
    private AtomicBoolean started = new AtomicBoolean(false);
    private boolean stopped;
    private boolean succeed;
    private LocalDateTime stoppedAt;
    private List<SimpleMutexTaskJobResult> results;
    private long historyKeepMillis;
    private long historyExpiredAt;

    public ConcurrentMutexTask(String key, Long lock, String title, String startedBy,
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

    public AtomicInteger getFinishedSteps() {
      return finishedSteps;
    }

    public void setFinishedSteps(AtomicInteger finishedSteps) {
      this.finishedSteps = finishedSteps;
    }

    public AtomicBoolean getStarted() {
      return started;
    }

    public void setStarted(AtomicBoolean started) {
      this.started = started;
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

    public List<SimpleMutexTaskJobResult> getResults() {
      return results;
    }

    public void setResults(
        List<SimpleMutexTaskJobResult> results) {
      this.results = results;
    }

    public long getHistoryKeepMillis() {
      return historyKeepMillis;
    }

    public void setHistoryKeepMillis(long historyKeepMillis) {
      this.historyKeepMillis = historyKeepMillis;
    }

    public long getHistoryExpiredAt() {
      return historyExpiredAt;
    }

    public void setHistoryExpiredAt(long historyExpiredAt) {
      this.historyExpiredAt = historyExpiredAt;
    }

    @Override
    public String toString() {
      return "ConcurrentMutexTask{" +
          "key='" + key + '\'' +
          ", lock=" + lock +
          ", title='" + title + '\'' +
          ", startedBy='" + startedBy + '\'' +
          ", startedAt=" + startedAt +
          ", totalSteps=" + totalSteps +
          ", started=" + started +
          ", stopped=" + stopped +
          ", succeed=" + succeed +
          ", stoppedAt=" + stoppedAt +
          ", results=" + results +
          ", historyKeepMillis=" + historyKeepMillis +
          ", historyExpiredAt=" + historyExpiredAt +
          '}';
    }
  }
}
