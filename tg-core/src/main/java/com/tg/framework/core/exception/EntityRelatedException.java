package com.tg.framework.core.exception;

public class EntityRelatedException extends BusinessException {

  private static final long serialVersionUID = -4920717049003521037L;

  public static final String PRESENT_CODE = "DomainObject#EntityRelated";

  public EntityRelatedException() {
    super(PRESENT_CODE);
  }

  public EntityRelatedException(String message) {
    super(PRESENT_CODE, message);
  }

  public EntityRelatedException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public EntityRelatedException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public EntityRelatedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public EntityRelatedException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public EntityRelatedException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public EntityRelatedException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public EntityRelatedException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public EntityRelatedException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }
}
