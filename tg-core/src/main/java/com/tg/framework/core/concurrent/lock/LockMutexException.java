package com.tg.framework.core.concurrent.lock;

import com.tg.framework.core.exception.BusinessException;

public class LockMutexException extends BusinessException {

  private static final long serialVersionUID = 1693173019040467612L;

  public static final String PRESENT_CODE = "Lock#LockMutex";


  public LockMutexException() {
    super(PRESENT_CODE);
  }

  public LockMutexException(String message) {
    super(PRESENT_CODE, message);
  }

  public LockMutexException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public LockMutexException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public LockMutexException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public LockMutexException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public LockMutexException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public LockMutexException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public LockMutexException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public LockMutexException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }
}
