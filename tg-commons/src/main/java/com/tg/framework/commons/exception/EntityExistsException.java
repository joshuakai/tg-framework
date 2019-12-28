package com.tg.framework.commons.exception;

public class EntityExistsException extends BusinessException {

  private static final long serialVersionUID = 5257541521579205897L;

  public static final String PRESENT_CODE = "Biz#EntityExists";

  public EntityExistsException() {
    super(PRESENT_CODE);
  }

  public EntityExistsException(String message) {
    super(PRESENT_CODE, message);
  }

  public EntityExistsException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public EntityExistsException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public EntityExistsException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public EntityExistsException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public EntityExistsException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public EntityExistsException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public EntityExistsException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public EntityExistsException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }

}
