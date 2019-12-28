package com.tg.framework.commons.exception;

public abstract class BusinessException extends NestedRuntimeException {

  private static final long serialVersionUID = 8738716899287151836L;

  public BusinessException(String code) {
    super(code);
  }

  public BusinessException(String code, String message) {
    super(code, message);
  }

  public BusinessException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }

  public BusinessException(String code, Throwable cause) {
    super(code, cause);
  }

  public BusinessException(String code, String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(code, message, cause, enableSuppression, writableStackTrace);
  }

  public BusinessException(String code, Object[] args) {
    super(code, args);
  }

  public BusinessException(String code, Object[] args, String message) {
    super(code, args, message);
  }

  public BusinessException(String code, Object[] args, String message, Throwable cause) {
    super(code, args, message, cause);
  }

  public BusinessException(String code, Object[] args, Throwable cause) {
    super(code, args, cause);
  }

  public BusinessException(String code, Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(code, args, message, cause, enableSuppression, writableStackTrace);
  }

}
