package com.tg.framework.commons.concurrent.task.support;

import java.time.LocalDateTime;

public final class SimpleMutexTaskJobResultBuilder {

  private String title;
  private LocalDateTime startedAt;
  private boolean started;
  private boolean stopped;
  private boolean succeed;
  private String message;
  private LocalDateTime stoppedAt;

  private SimpleMutexTaskJobResultBuilder() {
  }

  public static SimpleMutexTaskJobResultBuilder aSimpleMutexTaskJobResult() {
    return new SimpleMutexTaskJobResultBuilder();
  }

  public SimpleMutexTaskJobResultBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public SimpleMutexTaskJobResultBuilder withStartedAt(LocalDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  public SimpleMutexTaskJobResultBuilder withStarted(boolean started) {
    this.started = started;
    return this;
  }

  public SimpleMutexTaskJobResultBuilder withStopped(boolean stopped) {
    this.stopped = stopped;
    return this;
  }

  public SimpleMutexTaskJobResultBuilder withSucceed(boolean succeed) {
    this.succeed = succeed;
    return this;
  }

  public SimpleMutexTaskJobResultBuilder withMessage(String message) {
    this.message = message;
    return this;
  }

  public SimpleMutexTaskJobResultBuilder withStoppedAt(LocalDateTime stoppedAt) {
    this.stoppedAt = stoppedAt;
    return this;
  }

  public SimpleMutexTaskJobResult build() {
    SimpleMutexTaskJobResult simpleMutexTaskJobResult = new SimpleMutexTaskJobResult();
    simpleMutexTaskJobResult.setTitle(title);
    simpleMutexTaskJobResult.setStartedAt(startedAt);
    simpleMutexTaskJobResult.setStarted(started);
    simpleMutexTaskJobResult.setStopped(stopped);
    simpleMutexTaskJobResult.setSucceed(succeed);
    simpleMutexTaskJobResult.setMessage(message);
    simpleMutexTaskJobResult.setStoppedAt(stoppedAt);
    return simpleMutexTaskJobResult;
  }
}
