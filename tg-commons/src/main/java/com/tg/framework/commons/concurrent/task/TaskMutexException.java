package com.tg.framework.commons.concurrent.task;

import com.tg.framework.commons.NestedException;

public class TaskMutexException extends NestedException {

  private static final long serialVersionUID = -5242535001105058255L;

  private String key;

  public TaskMutexException(String key, String message) {
    super(message);
    this.key = key;
  }

  public TaskMutexException(String key, String message, boolean writableStackTrace) {
    super(message, writableStackTrace);
    this.key = key;
  }

  public TaskMutexException(String key, String message, Throwable cause) {
    super(message, cause);
    this.key = key;
  }

  public String getKey() {
    return key;
  }

}
