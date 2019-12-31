package com.tg.framework.commons.exception;

public abstract class NestedCodedException extends NestedException {

  private static final long serialVersionUID = 6101889632739037939L;

  private final String code;

  protected abstract String getPresentCode();

  public NestedCodedException() {
    code = getPresentCode();
  }

  public NestedCodedException(String message) {
    super(message);
    code = getPresentCode();
  }

  public NestedCodedException(String message, Throwable cause) {
    super(message, cause);
    code = getPresentCode();
  }

  public NestedCodedException(Throwable cause) {
    super(cause);
    code = getPresentCode();
  }

  public NestedCodedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    code = getPresentCode();
  }

  public String getCode() {
    return code;
  }
}
