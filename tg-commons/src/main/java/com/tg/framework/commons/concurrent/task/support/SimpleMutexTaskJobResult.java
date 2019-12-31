package com.tg.framework.commons.concurrent.task.support;

import com.tg.framework.commons.concurrent.task.MutexTaskJobResult;
import java.time.LocalDateTime;

public class SimpleMutexTaskJobResult implements MutexTaskJobResult {

  private String title;
  private LocalDateTime startedAt;
  private boolean started;
  private boolean stopped;
  private boolean succeed;
  private String message;
  private LocalDateTime stoppedAt;

  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public LocalDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(LocalDateTime startedAt) {
    this.startedAt = startedAt;
  }

  @Override
  public boolean isStarted() {
    return started;
  }

  public void setStarted(boolean started) {
    this.started = started;
  }

  @Override
  public boolean isStopped() {
    return stopped;
  }

  public void setStopped(boolean stopped) {
    this.stopped = stopped;
  }

  @Override
  public boolean isSucceed() {
    return succeed;
  }

  public void setSucceed(boolean succeed) {
    this.succeed = succeed;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public LocalDateTime getStoppedAt() {
    return stoppedAt;
  }

  public void setStoppedAt(LocalDateTime stoppedAt) {
    this.stoppedAt = stoppedAt;
  }

  @Override
  public String toString() {
    return "SimpleMutexTaskJobResult{" +
        "title='" + title + '\'' +
        ", startedAt=" + startedAt +
        ", started=" + started +
        ", stopped=" + stopped +
        ", succeed=" + succeed +
        ", message='" + message + '\'' +
        ", stoppedAt=" + stoppedAt +
        '}';
  }
}
