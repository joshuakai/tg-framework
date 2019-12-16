package com.tg.framework.core.exception;

public class ParamRequiredException extends ParameterException {

  private static final long serialVersionUID = -5792960084906277988L;

  public static final String PRESENT_CODE = "Param#Required";

  public ParamRequiredException(String paramName) {
    super(PRESENT_CODE, new Object[]{paramName});
  }

  public ParamRequiredException(String paramName, String message) {
    super(PRESENT_CODE, new Object[]{paramName}, message);
  }

  public ParamRequiredException(String paramName, String message, Throwable cause) {
    super(PRESENT_CODE, new Object[]{paramName}, message, cause);
  }

  public ParamRequiredException(String paramName, Throwable cause) {
    super(PRESENT_CODE, new Object[]{paramName}, cause);
  }

  public ParamRequiredException(String paramName, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, new Object[]{paramName}, message, cause, enableSuppression,
        writableStackTrace);
  }
}
