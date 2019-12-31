package com.tg.framework.commons.exception;

public abstract class NestedCodedRuntimeException extends NestedRuntimeException {

  private static final long serialVersionUID = 5739422377225544272L;

  private final String code;

  protected abstract String getPresentCode();

  public NestedCodedRuntimeException() {
    code = getPresentCode();
  }

  public NestedCodedRuntimeException(String message) {
    super(message);
    code = getPresentCode();
  }

  public NestedCodedRuntimeException(String message, Throwable cause) {
    super(message, cause);
    code = getPresentCode();
  }

  public NestedCodedRuntimeException(Throwable cause) {
    super(cause);
    code = getPresentCode();
  }

  public NestedCodedRuntimeException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    code = getPresentCode();
  }

  public String getCode() {
    return code;
  }
}
