package com.tg.framework.core.concurrent.task.redis;

import com.google.common.base.MoreObjects;
import com.tg.framework.commons.lang.ArrayOptional;
import com.tg.framework.core.concurrent.task.UniqueTask;
import com.tg.framework.core.concurrent.task.UniqueTaskException;
import com.tg.framework.core.concurrent.task.UniqueTaskService;
import com.tg.framework.core.concurrent.task.UniqueTaskStep;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

public class RedisUniqueTaskService implements UniqueTaskService {

  private static Logger logger = LoggerFactory.getLogger(RedisUniqueTaskService.class);

  private static final String DEFAULT_KEY_PREFIX = "unique_tasks:";
  private static final String CURRENT_SUFFIX = ":current";
  private static final String FINISHED_SUFFIX = ":completed";
  private static final String STEPS_SUFFIX = ":steps";
  private static final String PREVIOUS_SUFFIX = ":previous";

  private RedisTemplate redisTemplate;
  private String keyPrefix;

  public RedisUniqueTaskService(RedisTemplate redisTemplate) {
    this(redisTemplate, DEFAULT_KEY_PREFIX);
  }

  public RedisUniqueTaskService(RedisTemplate redisTemplate, String keyPrefix) {
    Assert.notNull(redisTemplate, "RedisTemplate must not be null.");
    this.redisTemplate = redisTemplate;
    this.keyPrefix = keyPrefix;
  }

  private String formatKey(String key, String suffix) {
    return Optional.ofNullable(keyPrefix).map(p -> p + key + suffix).orElse(key + suffix);
  }

  @Override
  public UniqueTask get(String key) {
    String currentKey = formatKey(key, CURRENT_SUFFIX);
    RedisUniqueTask redisUniqueTask = (RedisUniqueTask) redisTemplate.opsForValue().get(currentKey);
    if (redisUniqueTask != null) {
      String completedKey = formatKey(key, FINISHED_SUFFIX);
      String stepsKey = formatKey(key, STEPS_SUFFIX);
      long completedSteps = Optional.ofNullable(redisTemplate.opsForValue().get(completedKey))
          .map(Object::toString).map(Long::valueOf).orElse(0L);
      List<UniqueTaskStep> steps = Optional
          .ofNullable(redisTemplate.opsForList().range(stepsKey, 0, -1)).orElse(new ArrayList<>(0));
      steps.sort(UniqueTaskService::sortUniqueTaskStep);
      UniqueTask uniqueTask = new UniqueTask();
      uniqueTask.setKey(redisUniqueTask.getKey());
      uniqueTask.setId(redisUniqueTask.getId());
      uniqueTask.setTitle(redisUniqueTask.getTitle());
      uniqueTask.setTotalSteps(redisUniqueTask.getTotalSteps());
      uniqueTask.setStartAt(redisUniqueTask.getStartAt());
      uniqueTask.setStartBy(redisUniqueTask.getStartBy());
      uniqueTask.setCompletedSteps(completedSteps);
      uniqueTask.setSteps(steps);
      uniqueTask.setCompletedAt(redisUniqueTask.getCompletedAt());
      return uniqueTask;
    } else {
      String previousKey = formatKey(key, PREVIOUS_SUFFIX);
      return (UniqueTask) redisTemplate.opsForValue().get(previousKey);
    }
  }

  @Override
  public long start(String key, String title, long totalSteps, String startBy,
      long historyKeepMillis) {
    long id = RandomUtils.nextLong();
    RedisUniqueTask uniqueTask = new RedisUniqueTask();
    uniqueTask.setKey(key);
    uniqueTask.setId(id);
    uniqueTask.setTitle(title);
    uniqueTask.setTotalSteps(totalSteps);
    uniqueTask.setStartAt(LocalDateTime.now());
    uniqueTask.setStartBy(startBy);
    uniqueTask.setHistoryKeepMillis(historyKeepMillis);
    String currentKey = formatKey(key, CURRENT_SUFFIX);
    if (BooleanUtils.isNotTrue(redisTemplate.opsForValue().setIfAbsent(currentKey, uniqueTask))) {
      throw new UniqueTaskException(String.format("Unique task exists '%s'.", key));
    }
    String completedKey = formatKey(key, FINISHED_SUFFIX);
    String stepsKey = formatKey(key, STEPS_SUFFIX);
    redisTemplate.opsForValue().set(completedKey, 0L);
    redisTemplate.delete(stepsKey);
    logger.debug("Start unique task '{}' {} {}.", key, id, uniqueTask);
    return id;
  }

  @Override
  public long progress(String key, long id, long progressSteps, UniqueTaskStep... steps) {
    String currentKey = formatKey(key, CURRENT_SUFFIX);
    RedisUniqueTask uniqueTask = (RedisUniqueTask) redisTemplate.opsForValue().get(currentKey);
    if (uniqueTask == null || uniqueTask.getId() != id) {
      throw new UniqueTaskException(String.format("Unique task not found '%s' %d.", key, id));
    }
    if (progressSteps <= 0) {
      throw new UniqueTaskException(
          String.format("Invalid progress steps %d for '%s'.", progressSteps, key));
    }
    logger.debug("Progress unique task '{}' {} for {} steps {}.", key, id, progressSteps, steps);
    String completedKey = formatKey(key, FINISHED_SUFFIX);
    long finalCompletedSteps = Optional
        .ofNullable(redisTemplate.opsForValue().increment(completedKey, progressSteps)).orElse(0L);
    String stepsKey = formatKey(key, STEPS_SUFFIX);
    ArrayOptional.ofNullable(steps).ifPresent(
        s -> redisTemplate.opsForList().rightPushAll(stepsKey, Arrays.asList(s).toArray()));
    long totalSteps = uniqueTask.getTotalSteps();
    if (finalCompletedSteps > totalSteps) {
      logger.warn("Progress to much steps {}/{} for '%s'.", finalCompletedSteps, totalSteps, key);
    }
    boolean completed = finalCompletedSteps >= totalSteps;
    if (completed) {
      UniqueTask completedUniqueTask = new UniqueTask();
      completedUniqueTask.setKey(uniqueTask.getKey());
      completedUniqueTask.setId(uniqueTask.getId());
      completedUniqueTask.setTitle(uniqueTask.getTitle());
      completedUniqueTask.setTotalSteps(uniqueTask.getTotalSteps());
      completedUniqueTask.setStartAt(uniqueTask.getStartAt());
      completedUniqueTask.setStartBy(uniqueTask.getStartBy());
      completedUniqueTask.setCompletedSteps(finalCompletedSteps);
      List<UniqueTaskStep> sortedSteps = Optional
          .ofNullable(redisTemplate.opsForList().range(stepsKey, 0, -1)).orElse(new ArrayList<>(0));
      Collections.sort(sortedSteps, UniqueTaskService::sortUniqueTaskStep);
      completedUniqueTask.setSteps(sortedSteps);
      completedUniqueTask.setCompletedAt(LocalDateTime.now());
      String previousKey = formatKey(key, PREVIOUS_SUFFIX);
      if (uniqueTask.getHistoryKeepMillis() == -1L) {
        redisTemplate.opsForValue().set(previousKey, completedUniqueTask);
      } else {
        redisTemplate.opsForValue()
            .set(previousKey, completedUniqueTask, uniqueTask.getHistoryKeepMillis(),
                TimeUnit.MILLISECONDS);
      }
      redisTemplate
          .delete(Stream.of(currentKey, completedKey, stepsKey).collect(Collectors.toSet()));
      logger.debug("Complete unique task '{}' {} {}.", key, id, uniqueTask);
    }
    return finalCompletedSteps;
  }

  private static class RedisUniqueTask implements Serializable {

    private static final long serialVersionUID = 609424641114463696L;

    private String key;
    private long id;
    private String title;
    private long totalSteps;
    private LocalDateTime startAt;
    private String startBy;
    private LocalDateTime completedAt;
    private long historyKeepMillis;

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

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("key", key)
          .add("id", id)
          .add("title", title)
          .add("totalSteps", totalSteps)
          .add("startAt", startAt)
          .add("startBy", startBy)
          .add("completedAt", completedAt)
          .add("historyKeepMillis", historyKeepMillis)
          .toString();
    }
  }
}
