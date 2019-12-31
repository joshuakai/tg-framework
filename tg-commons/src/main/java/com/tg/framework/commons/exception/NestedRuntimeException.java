package com.tg.framework.commons.exception;

public abstract class NestedRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 8738716899287151836L;

  public NestedRuntimeException() {
  }

  public NestedRuntimeException(String message) {
    super(message);
  }

  public NestedRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public NestedRuntimeException(Throwable cause) {
    super(cause);
  }

  public NestedRuntimeException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
