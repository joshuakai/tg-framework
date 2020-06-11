package com.tg.framework.data.commons.exception;

public class EntityRelatedException extends ModelException {

  private static final long serialVersionUID = -4920717049003521037L;

  public static final String PRESENT_CODE = "Model#EntityRelated";

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public EntityRelatedException() {
    super();
  }

  public EntityRelatedException(String message) {
    super(message);
  }

  public EntityRelatedException(String message, Throwable cause) {
    super(message, cause);
  }

  public EntityRelatedException(Throwable cause) {
    super(cause);
  }

  public EntityRelatedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
