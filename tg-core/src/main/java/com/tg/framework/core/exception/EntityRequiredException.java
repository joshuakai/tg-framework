package com.tg.framework.core.exception;

public class EntityRequiredException extends ParameterException {

  private static final long serialVersionUID = -8408973709553472406L;

  public static final String PRESENT_CODE = "Param#EntityRequired";

  public EntityRequiredException() {
    super(PRESENT_CODE);
  }

  public EntityRequiredException(String message) {
    super(PRESENT_CODE, message);
  }

  public EntityRequiredException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public EntityRequiredException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public EntityRequiredException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public EntityRequiredException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public EntityRequiredException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public EntityRequiredException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public EntityRequiredException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public EntityRequiredException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }
}
