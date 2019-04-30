package com.tg.framework.core.exception;

public abstract class NestedException extends Exception {

  private static final long serialVersionUID = 8738716899287151836L;

  private String code;
  private Object[] args;

  public NestedException(String code) {
    this.code = code;
  }

  public NestedException(String code, String message) {
    super(message);
    this.code = code;
  }

  public NestedException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public NestedException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  public NestedException(String code, String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.code = code;
  }

  public NestedException(String code, Object[] args) {
    this.code = code;
    this.args = args;
  }

  public NestedException(String code, Object[] args, String message) {
    super(message);
    this.code = code;
    this.args = args;
  }

  public NestedException(String code, Object[] args, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
    this.args = args;
  }

  public NestedException(String code, Object[] args, Throwable cause) {
    super(cause);
    this.code = code;
    this.args = args;
  }

  public NestedException(String code, Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.code = code;
    this.args = args;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Object[] getArgs() {
    return args;
  }

  public void setArgs(Object[] args) {
    this.args = args;
  }
}
