package com.tg.framework.commons.concurrent.task.standalone;

import com.google.common.base.MoreObjects;
import com.tg.framework.commons.lang.ArrayOptional;
import com.tg.framework.commons.concurrent.task.UniqueTask;
import com.tg.framework.commons.concurrent.task.UniqueTaskException;
import com.tg.framework.commons.concurrent.task.UniqueTaskService;
import com.tg.framework.commons.concurrent.task.UniqueTaskStep;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultUniqueTaskService implements UniqueTaskService {

  private static final long ENDLESS_TIMEOUT_MILLIS = -1L;

  private static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 5000L;

  private static Logger logger = LoggerFactory.getLogger(DefaultUniqueTaskService.class);

  private long timeBetweenEvictionRunsMillis;

  private final ConcurrentHashMap<String, ConcurrentUniqueTask> uniqueTaskHolder = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, ConcurrentUniqueTask> previousUniqueTaskHolder = new ConcurrentHashMap<>();

  public DefaultUniqueTaskService() {
    this(DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
  }

  public DefaultUniqueTaskService(long timeBetweenEvictionRunsMillis) {
    this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    startExpireThread();
  }

  private void startExpireThread() {
    Thread thread = new Thread(() -> {
      while (true) {
        logger.debug("Check expired UniqueTask.");
        Iterator<Entry<String, ConcurrentUniqueTask>> ite = previousUniqueTaskHolder.entrySet()
            .iterator();
        while (ite.hasNext()) {
          Optional.ofNullable(ite.next().getValue())
              .filter(DefaultUniqueTaskService::isExpired)
              .ifPresent(ut -> {
                logger.debug("Expired UniqueTask found {}.", ut);
                ite.remove();
              });
        }
        try {
          Thread.sleep(timeBetweenEvictionRunsMillis);
        } catch (InterruptedException e) {
          logger.error("Expire thread was interrupted.", e);
        }
      }
    });
    thread.setDaemon(true);
    thread.start();
  }

  @Override
  public UniqueTask get(String key) {
    ConcurrentUniqueTask uniqueTask = uniqueTaskHolder.get(key);
    if (uniqueTask != null) {
      Collections.sort(uniqueTask.getSteps(), UniqueTaskService::sortUniqueTaskStep);
    } else {
      uniqueTask = previousUniqueTaskHolder.get(key);
    }
    return Optional.ofNullable(uniqueTask).map(DefaultUniqueTaskService::mapUniqueTask)
        .orElse(null);
  }

  @Override
  public long start(String key, String title, long totalSteps, String startBy,
      long historyKeepMillis) {
    long id = RandomUtils.nextLong();
    ConcurrentUniqueTask uniqueTask = new ConcurrentUniqueTask();
    uniqueTask.setKey(key);
    uniqueTask.setId(id);
    uniqueTask.setTitle(title);
    uniqueTask.setTotalSteps(totalSteps);
    uniqueTask.setStartAt(LocalDateTime.now());
    uniqueTask.setStartBy(startBy);
    uniqueTask.setCompletedSteps(new AtomicLong());
    uniqueTask.setSteps(Collections.synchronizedList(new ArrayList<>()));
    uniqueTask.setHistoryKeepMillis(historyKeepMillis);
    ConcurrentUniqueTask previous;
    if ((previous = uniqueTaskHolder.putIfAbsent(key, uniqueTask)) != null) {
      throw new UniqueTaskException(
          String.format("Unique task exists '%s' %d.", key, previous.getId()));
    }
    logger.debug("Start unique task '{}' {} {}.", key, id, uniqueTask);
    return id;
  }


  @Override
  public long progress(String key, long id, long progressSteps, UniqueTaskStep... steps) {
    ConcurrentUniqueTask uniqueTask = uniqueTaskHolder.get(key);
    if (uniqueTask == null || uniqueTask.getId() != id) {
      throw new UniqueTaskException(String.format("Unique task not found '%s' %d.", key, id));
    }
    if (progressSteps <= 0) {
      throw new UniqueTaskException(
          String.format("Invalid progress steps %d for '%s'.", progressSteps, key));
    }
    logger.debug("Progress unique task '{}' {} for {} steps {}.", key, id, progressSteps, steps);
    AtomicLong completedSteps = uniqueTask.getCompletedSteps();
    while (progressSteps > 0) {
      completedSteps.incrementAndGet();
      progressSteps--;
    }
    ArrayOptional.ofNullable(steps).map(Arrays::asList)
        .ifPresent(s -> uniqueTask.getSteps().addAll(s));
    long finalCompletedSteps = completedSteps.get();
    long totalSteps = uniqueTask.getTotalSteps();
    if (finalCompletedSteps > totalSteps) {
      logger.warn("Progress to much steps {}/{} for '%s'.", finalCompletedSteps, totalSteps, key);
    }
    boolean completed = finalCompletedSteps >= totalSteps;
    if (completed) {
      uniqueTask.setCompletedAt(LocalDateTime.now());
      long historyKeepMillis = uniqueTask.getHistoryKeepMillis();
      uniqueTask.setHistoryExpiredAt(historyKeepMillis == ENDLESS_TIMEOUT_MILLIS ? historyKeepMillis
          : LocalDateTime.now().getNano() + historyKeepMillis);
      Collections.sort(uniqueTask.getSteps(), UniqueTaskService::sortUniqueTaskStep);
      previousUniqueTaskHolder.put(key, uniqueTask);
      uniqueTaskHolder.remove(key);
      logger.debug("Complete unique task '{}' {} {}.", key, id, uniqueTask);
    }
    return finalCompletedSteps;
  }

  private static UniqueTask mapUniqueTask(ConcurrentUniqueTask concurrentUniqueTask) {
    UniqueTask uniqueTask = new UniqueTask();
    uniqueTask.setKey(concurrentUniqueTask.getKey());
    uniqueTask.setId(concurrentUniqueTask.getId());
    uniqueTask.setTitle(concurrentUniqueTask.getTitle());
    uniqueTask.setTotalSteps(concurrentUniqueTask.getTotalSteps());
    uniqueTask.setStartAt(concurrentUniqueTask.getStartAt());
    uniqueTask.setStartBy(concurrentUniqueTask.getStartBy());
    uniqueTask.setCompletedSteps(concurrentUniqueTask.getCompletedSteps().get());
    uniqueTask.setSteps(concurrentUniqueTask.getSteps());
    uniqueTask.setCompletedAt(concurrentUniqueTask.getCompletedAt());
    return uniqueTask;
  }

  private static boolean isExpired(ConcurrentUniqueTask uniqueTask) {
    return uniqueTask.getHistoryExpiredAt() != ENDLESS_TIMEOUT_MILLIS
        && LocalDateTime.now().getNano() >= uniqueTask.getHistoryExpiredAt();
  }

  private static class ConcurrentUniqueTask implements Serializable {

    private static final long serialVersionUID = -2384851428436645560L;

    private String key;
    private long id;
    private String title;
    private long totalSteps;
    private LocalDateTime startAt;
    private String startBy;
    private AtomicLong completedSteps;
    private List<UniqueTaskStep> steps;
    private LocalDateTime completedAt;
    private long historyKeepMillis;
    private long historyExpiredAt;

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public long getId() {
      return id;
    }

    public void setId(long id) {
      this.id = id;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public long getTotalSteps() {
      return totalSteps;
    }

    public void setTotalSteps(long totalSteps) {
      this.totalSteps = totalSteps;
    }

    public LocalDateTime getStartAt() {
      return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
      this.startAt = startAt;
    }

    public String getStartBy() {
      return startBy;
    }

    public void setStartBy(String startBy) {
      this.startBy = startBy;
    }

    public AtomicLong getCompletedSteps() {
      return completedSteps;
    }

    public void setCompletedSteps(AtomicLong completedSteps) {
      this.completedSteps = completedSteps;
    }

    public List<UniqueTaskStep> getSteps() {
      return steps;
    }

    public void setSteps(List<UniqueTaskStep> steps) {
      this.steps = steps;
    }

    public LocalDateTime getCompletedAt() {
      return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
      this.completedAt = completedAt;
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
      return MoreObjects.toStringHelper(this)
          .add("key", key)
          .add("id", id)
          .add("title", title)
          .add("totalSteps", totalSteps)
          .add("startAt", startAt)
          .add("startBy", startBy)
          .add("completedSteps", completedSteps)
          .add("steps", steps)
          .add("completedAt", completedAt)
          .add("historyKeepMillis", historyKeepMillis)
          .add("historyExpiredAt", historyExpiredAt)
          .toString();
    }
  }

}
