package com.tg.framework.commons.exception;

public abstract class ParamException extends NestedCodedRuntimeException {

  private static final long serialVersionUID = 5620993539920566619L;

  private String paramName;

  public ParamException(String paramName) {
    super();
    this.paramName = paramName;
  }

  public ParamException(String message, String paramName) {
    super(message);
    this.paramName = paramName;
  }

  public ParamException(String message, Throwable cause, String paramName) {
    super(message, cause);
    this.paramName = paramName;
  }

  public ParamException(Throwable cause, String paramName) {
    super(cause);
    this.paramName = paramName;
  }

  public ParamException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String paramName) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.paramName = paramName;
  }

  public String getParamName() {
    return paramName;
  }
}
