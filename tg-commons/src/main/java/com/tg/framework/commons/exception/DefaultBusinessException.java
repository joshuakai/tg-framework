package com.tg.framework.commons.exception;

public class DefaultBusinessException extends BusinessException {

  public static final String PRESENT_CODE = "Biz";

  public DefaultBusinessException() {
    super(PRESENT_CODE);
  }

  public DefaultBusinessException(String message) {
    super(PRESENT_CODE, message);
  }

  public DefaultBusinessException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public DefaultBusinessException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public DefaultBusinessException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public DefaultBusinessException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public DefaultBusinessException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public DefaultBusinessException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public DefaultBusinessException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public DefaultBusinessException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }
}
