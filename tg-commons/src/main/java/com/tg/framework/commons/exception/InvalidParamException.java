package com.tg.framework.commons.exception;

public class InvalidParamException extends ParameterException {

  private static final long serialVersionUID = -5792960084906277988L;

  public static final String PRESENT_CODE = "Param#Invalid";

  public InvalidParamException(String paramName, Object paramValue) {
    super(PRESENT_CODE, new Object[]{paramName, paramValue});
  }

  public InvalidParamException(String paramName, Object paramValue, String message) {
    super(PRESENT_CODE, new Object[]{paramName, paramValue}, message);
  }

  public InvalidParamException(String paramName, Object paramValue, String message,
      Throwable cause) {
    super(PRESENT_CODE, new Object[]{paramName, paramValue}, message, cause);
  }

  public InvalidParamException(String paramName, Object paramValue, Throwable cause) {
    super(PRESENT_CODE, new Object[]{paramName, paramValue}, cause);
  }

  public InvalidParamException(String paramName, Object paramValue, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, new Object[]{paramName, paramValue}, message, cause, enableSuppression,
        writableStackTrace);
  }
}
