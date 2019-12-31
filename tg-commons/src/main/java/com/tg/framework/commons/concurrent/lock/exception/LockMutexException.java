package com.tg.framework.commons.concurrent.lock.exception;

public class LockMutexException extends LockException {

  private static final long serialVersionUID = -3496610600475061935L;

  public static final String PRESENT_CODE = "Lock#MUTEX";

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public LockMutexException(String key) {
    super(key);
  }

  public LockMutexException(String message, String key) {
    super(message, key);
  }

  public LockMutexException(String message, Throwable cause, String key) {
    super(message, cause, key);
  }

  public LockMutexException(Throwable cause, String key) {
    super(cause, key);
  }

  public LockMutexException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String key) {
    super(message, cause, enableSuppression, writableStackTrace, key);
  }
}
