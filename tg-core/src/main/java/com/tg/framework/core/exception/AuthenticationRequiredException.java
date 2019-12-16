package com.tg.framework.core.exception;

public class AuthenticationRequiredException extends NestedRuntimeException {

  private static final long serialVersionUID = -7074344870076159125L;
  public static final String PRESENT_CODE = "Security#AuthenticationRequired";

  public AuthenticationRequiredException() {
    super(PRESENT_CODE);
  }

  public AuthenticationRequiredException(String message) {
    super(PRESENT_CODE, message);
  }

  public AuthenticationRequiredException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public AuthenticationRequiredException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public AuthenticationRequiredException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public AuthenticationRequiredException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public AuthenticationRequiredException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public AuthenticationRequiredException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public AuthenticationRequiredException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public AuthenticationRequiredException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }
}
