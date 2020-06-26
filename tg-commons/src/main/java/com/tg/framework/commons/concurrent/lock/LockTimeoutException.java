package com.tg.framework.commons.concurrent.lock;

public class LockTimeoutException extends LockException {

  private static final long serialVersionUID = -1267242451108932305L;

  public LockTimeoutException(String key, String message) {
    super(key, message);
  }

  public LockTimeoutException(String key, String message, boolean writableStackTrace) {
    super(key, message, writableStackTrace);
  }

  public LockTimeoutException(String key, String message, Throwable cause) {
    super(key, message, cause);
  }
}
