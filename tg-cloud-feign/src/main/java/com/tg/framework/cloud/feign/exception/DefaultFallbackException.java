package com.tg.framework.cloud.feign.exception;

public class DefaultFallbackException extends FallbackException {

  private static final long serialVersionUID = 2095204345814176600L;

  public static final String PRESENT_CODE = "Fallback#Default";

  public DefaultFallbackException() {
    super(PRESENT_CODE);
  }

  public DefaultFallbackException(String message) {
    super(PRESENT_CODE, message);
  }

  public DefaultFallbackException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public DefaultFallbackException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public DefaultFallbackException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public DefaultFallbackException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public DefaultFallbackException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public DefaultFallbackException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public DefaultFallbackException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public DefaultFallbackException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }

}
