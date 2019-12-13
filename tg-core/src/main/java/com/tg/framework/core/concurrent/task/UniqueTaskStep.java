package com.tg.framework.core.concurrent.task;

import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.time.LocalDateTime;

public class UniqueTaskStep implements Serializable {

  private static final long serialVersionUID = -7476981605905798488L;

  private LocalDateTime completedAt;
  private boolean succeed;
  private String message;

  public UniqueTaskStep() {
  }

  public UniqueTaskStep(LocalDateTime completedAt, boolean succeed, String message) {
    this.completedAt = completedAt;
    this.succeed = succeed;
    this.message = message;
  }

  public LocalDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(LocalDateTime completedAt) {
    this.completedAt = completedAt;
  }

  public boolean isSucceed() {
    return succeed;
  }

  public void setSucceed(boolean succeed) {
    this.succeed = succeed;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("completedAt", completedAt)
        .add("succeed", succeed)
        .add("message", message)
        .toString();
  }
}
