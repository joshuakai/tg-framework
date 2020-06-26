package com.tg.framework.commons;

public abstract class NestedException extends Exception {

  private static final long serialVersionUID = -4548130420524918254L;

  public NestedException(String message) {
    this(message, false);
  }

  public NestedException(String message, boolean writableStackTrace) {
    super(message, null, false, writableStackTrace);
  }

  public NestedException(String message, Throwable cause) {
    super(message, cause, false, true);
  }

}
