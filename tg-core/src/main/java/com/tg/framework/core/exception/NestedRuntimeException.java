package com.tg.framework.core.exception;

public class NestedRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 8738716899287151836L;

  private String code;
  private Object[] args;

  public NestedRuntimeException(String code) {
    this.code = code;
  }

  public NestedRuntimeException(String code, String message) {
    super(message);
    this.code = code;
  }

  public NestedRuntimeException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public NestedRuntimeException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  public NestedRuntimeException(String code, String message, Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.code = code;
  }

  public NestedRuntimeException(String code, Object[] args) {
    this.code = code;
    this.args = args;
  }

  public NestedRuntimeException(String code, Object[] args, String message) {
    super(message);
    this.code = code;
    this.args = args;
  }

  public NestedRuntimeException(String code, Object[] args, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
    this.args = args;
  }

  public NestedRuntimeException(String code, Object[] args, Throwable cause) {
    super(cause);
    this.code = code;
    this.args = args;
  }

  public NestedRuntimeException(String code, Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.code = code;
    this.args = args;
  }
}
