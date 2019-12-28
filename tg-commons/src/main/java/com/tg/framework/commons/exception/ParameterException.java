package com.tg.framework.commons.exception;

public abstract class ParameterException extends NestedRuntimeException {

  private static final long serialVersionUID = 5620993539920566619L;

  public ParameterException(String code) {
    super(code);
  }

  public ParameterException(String code, String message) {
    super(code, message);
  }

  public ParameterException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }

  public ParameterException(String code, Throwable cause) {
    super(code, cause);
  }

  public ParameterException(String code, String message, Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(code, message, cause, enableSuppression, writableStackTrace);
  }

  public ParameterException(String code, Object[] args) {
    super(code, args);
  }

  public ParameterException(String code, Object[] args, String message) {
    super(code, args, message);
  }

  public ParameterException(String code, Object[] args, String message, Throwable cause) {
    super(code, args, message, cause);
  }

  public ParameterException(String code, Object[] args, Throwable cause) {
    super(code, args, cause);
  }

  public ParameterException(String code, Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(code, args, message, cause, enableSuppression, writableStackTrace);
  }
}
