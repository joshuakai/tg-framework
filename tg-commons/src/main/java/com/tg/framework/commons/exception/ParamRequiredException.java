package com.tg.framework.commons.exception;

public class ParamRequiredException extends ParamException {

  private static final long serialVersionUID = -5792960084906277988L;

  public static final String PRESENT_CODE = "Param#Required";

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public ParamRequiredException(String paramName) {
    super(paramName);
  }

  public ParamRequiredException(String message, String paramName) {
    super(message, paramName);
  }

  public ParamRequiredException(String message, Throwable cause, String paramName) {
    super(message, cause, paramName);
  }

  public ParamRequiredException(Throwable cause, String paramName) {
    super(cause, paramName);
  }

  public ParamRequiredException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String paramName) {
    super(message, cause, enableSuppression, writableStackTrace, paramName);
  }
}
