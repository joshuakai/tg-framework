package com.tg.framework.commons.concurrent.task.support;

import java.time.LocalDateTime;
import java.util.List;

public final class SimpleMutexTaskBuilder {

  private String key;
  private Long lock;
  private String title;
  private String startedBy;
  private LocalDateTime startedAt;
  private String executeNode;
  private int totalSteps;
  private int finishedSteps;
  private boolean started;
  private boolean stopped;
  private boolean succeed;
  private LocalDateTime stoppedAt;
  private List<SimpleMutexTaskJobResult> results;

  private SimpleMutexTaskBuilder() {
  }

  public static SimpleMutexTaskBuilder aSimpleMutexTask() {
    return new SimpleMutexTaskBuilder();
  }

  public SimpleMutexTaskBuilder withKey(String key) {
    this.key = key;
    return this;
  }

  public SimpleMutexTaskBuilder withLock(Long lock) {
    this.lock = lock;
    return this;
  }

  public SimpleMutexTaskBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public SimpleMutexTaskBuilder withStartedBy(String startedBy) {
    this.startedBy = startedBy;
    return this;
  }

  public SimpleMutexTaskBuilder withStartedAt(LocalDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  public SimpleMutexTaskBuilder withExecuteNode(String executeNode) {
    this.executeNode = executeNode;
    return this;
  }

  public SimpleMutexTaskBuilder withTotalSteps(int totalSteps) {
    this.totalSteps = totalSteps;
    return this;
  }

  public SimpleMutexTaskBuilder withFinishedSteps(int finishedSteps) {
    this.finishedSteps = finishedSteps;
    return this;
  }

  public SimpleMutexTaskBuilder withStarted(boolean started) {
    this.started = started;
    return this;
  }

  public SimpleMutexTaskBuilder withStopped(boolean stopped) {
    this.stopped = stopped;
    return this;
  }

  public SimpleMutexTaskBuilder withSucceed(boolean succeed) {
    this.succeed = succeed;
    return this;
  }

  public SimpleMutexTaskBuilder withStoppedAt(LocalDateTime stoppedAt) {
    this.stoppedAt = stoppedAt;
    return this;
  }

  public SimpleMutexTaskBuilder withResults(List<SimpleMutexTaskJobResult> results) {
    this.results = results;
    return this;
  }

  public SimpleMutexTask build() {
    SimpleMutexTask simpleMutexTask = new SimpleMutexTask();
    simpleMutexTask.setKey(key);
    simpleMutexTask.setLock(lock);
    simpleMutexTask.setTitle(title);
    simpleMutexTask.setStartedBy(startedBy);
    simpleMutexTask.setStartedAt(startedAt);
    simpleMutexTask.setExecuteNode(executeNode);
    simpleMutexTask.setTotalSteps(totalSteps);
    simpleMutexTask.setFinishedSteps(finishedSteps);
    simpleMutexTask.setStarted(started);
    simpleMutexTask.setStopped(stopped);
    simpleMutexTask.setSucceed(succeed);
    simpleMutexTask.setStoppedAt(stoppedAt);
    simpleMutexTask.setResults(results);
    return simpleMutexTask;
  }
}
