package com.tg.framework.commons.exception;

public class ParamInvalidException extends ParamException {

  private static final long serialVersionUID = -5792960084906277988L;

  public static final String PRESENT_CODE = "Param#Invalid";

  private Object paramValue;

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public ParamInvalidException(String paramName, Object paramValue) {
    super(paramName);
    this.paramValue = paramValue;
  }

  public ParamInvalidException(String message, String paramName, Object paramValue) {
    super(message, paramName);
    this.paramValue = paramValue;
  }

  public ParamInvalidException(String message, Throwable cause, String paramName,
      Object paramValue) {
    super(message, cause, paramName);
    this.paramValue = paramValue;
  }

  public ParamInvalidException(Throwable cause, String paramName, Object paramValue) {
    super(cause, paramName);
    this.paramValue = paramValue;
  }

  public ParamInvalidException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String paramName, Object paramValue) {
    super(message, cause, enableSuppression, writableStackTrace, paramName);
    this.paramValue = paramValue;
  }

  public Object getParamValue() {
    return paramValue;
  }
}
