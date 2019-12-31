package com.tg.framework.commons.exception;

public class BusinessException extends NestedRuntimeException {

  private Object[] args;

  public BusinessException() {
  }

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }

  public BusinessException(Throwable cause) {
    super(cause);
  }

  public BusinessException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public BusinessException(Object... args) {
    this.args = args;
  }

  public BusinessException(String message, Object... args) {
    super(message);
    this.args = args;
  }

  public BusinessException(String message, Throwable cause, Object... args) {
    super(message, cause);
    this.args = args;
  }

  public BusinessException(Throwable cause, Object... args) {
    super(cause);
    this.args = args;
  }

  public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.args = args;
  }

  public Object[] getArgs() {
    return args;
  }
}
