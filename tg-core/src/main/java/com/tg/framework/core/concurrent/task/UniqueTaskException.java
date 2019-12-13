package com.tg.framework.core.concurrent.task;

import com.tg.framework.core.exception.BusinessException;

public class UniqueTaskException extends BusinessException {

  private static final long serialVersionUID = -689229004283230178L;

  public static final String PRESENT_CODE = "Task#UniqueTask";


  public UniqueTaskException() {
    super(PRESENT_CODE);
  }

  public UniqueTaskException(String message) {
    super(PRESENT_CODE, message);
  }

  public UniqueTaskException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public UniqueTaskException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public UniqueTaskException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public UniqueTaskException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public UniqueTaskException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public UniqueTaskException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public UniqueTaskException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public UniqueTaskException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }
}
