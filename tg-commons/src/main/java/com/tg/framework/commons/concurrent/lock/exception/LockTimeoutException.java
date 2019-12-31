package com.tg.framework.commons.concurrent.lock.exception;

public class LockTimeoutException extends LockException {

  private static final long serialVersionUID = -1267242451108932305L;

  public static final String PRESENT_CODE = "Lock#Timeout";

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public LockTimeoutException(String key) {
    super(key);
  }

  public LockTimeoutException(String message, String key) {
    super(message, key);
  }

  public LockTimeoutException(String message, Throwable cause, String key) {
    super(message, cause, key);
  }

  public LockTimeoutException(Throwable cause, String key) {
    super(cause, key);
  }

  public LockTimeoutException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String key) {
    super(message, cause, enableSuppression, writableStackTrace, key);
  }
}
