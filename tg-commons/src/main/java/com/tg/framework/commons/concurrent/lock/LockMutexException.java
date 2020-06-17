package com.tg.framework.commons.concurrent.lock;

public class LockMutexException extends LockException {

  private static final long serialVersionUID = -3496610600475061935L;

  public LockMutexException(String key, String message) {
    super(key, message);
  }

  public LockMutexException(String key, String message, boolean writableStackTrace) {
    super(key, message, writableStackTrace);
  }

  public LockMutexException(String key, String message, Throwable cause) {
    super(key, message, cause);
  }
}
