package com.tg.framework.core.exception;

public class ResourceNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = -6098581242915032532L;

  public static final String PRESENT_CODE = "Resource#ResourceNotFound";

  public ResourceNotFoundException() {
    super(PRESENT_CODE);
  }

  public ResourceNotFoundException(String message) {
    super(PRESENT_CODE, message);
  }

  public ResourceNotFoundException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public ResourceNotFoundException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public ResourceNotFoundException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public ResourceNotFoundException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public ResourceNotFoundException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public ResourceNotFoundException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public ResourceNotFoundException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }

}
