package com.tg.framework.commons.exception;

public class PrimaryKeyRequiredException extends ParameterException {

  private static final long serialVersionUID = -8477154087002809241L;

  public static final String PRESENT_CODE = "Param#PrimaryKeyRequired";

  public PrimaryKeyRequiredException() {
    super(PRESENT_CODE);
  }

  public PrimaryKeyRequiredException(String message) {
    super(PRESENT_CODE, message);
  }

  public PrimaryKeyRequiredException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public PrimaryKeyRequiredException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public PrimaryKeyRequiredException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public PrimaryKeyRequiredException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public PrimaryKeyRequiredException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public PrimaryKeyRequiredException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public PrimaryKeyRequiredException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public PrimaryKeyRequiredException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }

}
