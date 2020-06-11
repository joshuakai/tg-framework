package com.tg.framework.data.commons.exception;

public class EntityExistsException extends ModelException {

  private static final long serialVersionUID = 5257541521579205897L;

  public static final String PRESENT_CODE = "Model#EntityExists";

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public EntityExistsException() {
    super();
  }

  public EntityExistsException(String message) {
    super(message);
  }

  public EntityExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public EntityExistsException(Throwable cause) {
    super(cause);
  }

  public EntityExistsException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
