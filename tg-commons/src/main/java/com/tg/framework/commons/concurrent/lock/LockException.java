package com.tg.framework.commons.concurrent.lock;

import com.tg.framework.commons.NestedException;

public abstract class LockException extends NestedException {

  private static final long serialVersionUID = -3625877680581250297L;

  private String key;

  public LockException(String key, String message) {
    super(message);
    this.key = key;
  }

  public LockException(String key, String message, boolean writableStackTrace) {
    super(message, writableStackTrace);
    this.key = key;
  }

  public LockException(String key, String message, Throwable cause) {
    super(message, cause);
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
