package com.tg.framework.core.concurrent.lock;

import com.tg.framework.core.exception.BusinessException;

public class LockTimeoutException extends BusinessException {

  private static final long serialVersionUID = -5619886320436095278L;

  public static final String PRESENT_CODE = "Lock#LockTimeout";

  public LockTimeoutException() {
    super(PRESENT_CODE);
  }

  public LockTimeoutException(String message) {
    super(PRESENT_CODE, message);
  }

  public LockTimeoutException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public LockTimeoutException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public LockTimeoutException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public LockTimeoutException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public LockTimeoutException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public LockTimeoutException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public LockTimeoutException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public LockTimeoutException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }
}
