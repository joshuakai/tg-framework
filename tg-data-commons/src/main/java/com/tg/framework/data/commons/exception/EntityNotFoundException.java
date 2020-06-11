package com.tg.framework.data.commons.exception;

public class EntityNotFoundException extends ModelException {

  private static final long serialVersionUID = -8589881185218225549L;

  public static final String PRESENT_CODE = "Model#EntityNotFound";

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public EntityNotFoundException() {
    super();
  }

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public EntityNotFoundException(Throwable cause) {
    super(cause);
  }

  public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
