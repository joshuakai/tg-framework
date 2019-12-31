package com.tg.framework.commons.exception;

public class PrimaryKeyRequiredException extends ParamRequiredException {

  private static final long serialVersionUID = -8477154087002809241L;

  public static final String PRESENT_CODE = "Param#Required#PK";

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public PrimaryKeyRequiredException(String paramName) {
    super(paramName);
  }

  public PrimaryKeyRequiredException(String message, String paramName) {
    super(message, paramName);
  }

  public PrimaryKeyRequiredException(String message, Throwable cause, String paramName) {
    super(message, cause, paramName);
  }

  public PrimaryKeyRequiredException(Throwable cause, String paramName) {
    super(cause, paramName);
  }

  public PrimaryKeyRequiredException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String paramName) {
    super(message, cause, enableSuppression, writableStackTrace, paramName);
  }
}
