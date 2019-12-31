package com.tg.framework.commons.exception;

public abstract class NestedException extends Exception {

  private static final long serialVersionUID = 8738716899287151836L;

  public NestedException() {
  }

  public NestedException(String message) {
    super(message);
  }

  public NestedException(String message, Throwable cause) {
    super(message, cause);
  }

  public NestedException(Throwable cause) {
    super(cause);
  }

  public NestedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
