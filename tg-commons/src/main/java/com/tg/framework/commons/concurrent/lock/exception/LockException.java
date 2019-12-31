package com.tg.framework.commons.concurrent.lock.exception;

import com.tg.framework.commons.exception.NestedCodedException;

public abstract class LockException extends NestedCodedException {

  private static final long serialVersionUID = -3625877680581250297L;

  private String key;

  public LockException(String key) {
    super();
    this.key = key;
  }

  public LockException(String message, String key) {
    super(message);
    this.key = key;
  }

  public LockException(String message, Throwable cause, String key) {
    super(message, cause);
    this.key = key;
  }

  public LockException(Throwable cause, String key) {
    super(cause);
    this.key = key;
  }

  public LockException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String key) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}
