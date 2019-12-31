package com.tg.framework.commons.exception;

public class UniqueKeyDuplicateException extends ParamException {

  private static final long serialVersionUID = -2083685513356399099L;

  public static final String PRESENT_CODE = "Param#Duplicate#UK";

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public UniqueKeyDuplicateException(String paramName) {
    super(paramName);
  }

  public UniqueKeyDuplicateException(String message, String paramName) {
    super(message, paramName);
  }

  public UniqueKeyDuplicateException(String message, Throwable cause, String paramName) {
    super(message, cause, paramName);
  }

  public UniqueKeyDuplicateException(Throwable cause, String paramName) {
    super(cause, paramName);
  }

  public UniqueKeyDuplicateException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String paramName) {
    super(message, cause, enableSuppression, writableStackTrace, paramName);
  }
}
