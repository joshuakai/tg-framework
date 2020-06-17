package com.tg.framework.commons;

public abstract class NestedRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 8135400229186491454L;

  public NestedRuntimeException(String message) {
    this(message, false);
  }

  public NestedRuntimeException(String message, boolean writableStackTrace) {
    super(message, null, false, writableStackTrace);
  }

  public NestedRuntimeException(String message, Throwable cause) {
    super(message, cause, false, true);
  }

}
