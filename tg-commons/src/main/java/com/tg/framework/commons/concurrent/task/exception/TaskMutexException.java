package com.tg.framework.commons.concurrent.task.exception;

import com.tg.framework.commons.exception.NestedCodedException;

public class TaskMutexException extends NestedCodedException {

  private static final long serialVersionUID = -5242535001105058255L;

  public static final String PRESENT_CODE = "Task#MUTEX";

  private String key;

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public TaskMutexException(String key) {
    this.key = key;
  }

  public TaskMutexException(String message, String key) {
    super(message);
    this.key = key;
  }

  public TaskMutexException(String message, Throwable cause, String key) {
    super(message, cause);
    this.key = key;
  }

  public TaskMutexException(Throwable cause, String key) {
    super(cause);
    this.key = key;
  }

  public TaskMutexException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String key) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
