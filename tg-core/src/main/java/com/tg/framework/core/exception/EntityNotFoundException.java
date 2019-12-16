package com.tg.framework.core.exception;

public class EntityNotFoundException extends BusinessException {

  private static final long serialVersionUID = -8589881185218225549L;

  public static final String PRESENT_CODE = "DomainObject#EntityNotFound";

  public EntityNotFoundException() {
    super(PRESENT_CODE);
  }

  public EntityNotFoundException(String message) {
    super(PRESENT_CODE, message);
  }

  public EntityNotFoundException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public EntityNotFoundException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public EntityNotFoundException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public EntityNotFoundException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public EntityNotFoundException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public EntityNotFoundException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public EntityNotFoundException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }

}
