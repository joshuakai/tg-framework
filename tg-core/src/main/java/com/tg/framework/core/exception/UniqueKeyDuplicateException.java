package com.tg.framework.core.exception;

public class UniqueKeyDuplicateException extends BusinessException {

  private static final long serialVersionUID = -2083685513356399099L;

  public static final String PRESENT_CODE = "Biz#UniqueKeyDuplicate";

  public UniqueKeyDuplicateException() {
    super(PRESENT_CODE);
  }

  public UniqueKeyDuplicateException(String message) {
    super(PRESENT_CODE, message);
  }

  public UniqueKeyDuplicateException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public UniqueKeyDuplicateException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public UniqueKeyDuplicateException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public UniqueKeyDuplicateException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public UniqueKeyDuplicateException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public UniqueKeyDuplicateException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public UniqueKeyDuplicateException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public UniqueKeyDuplicateException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }


}
