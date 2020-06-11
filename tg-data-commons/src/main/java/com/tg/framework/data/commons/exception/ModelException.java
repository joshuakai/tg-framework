package com.tg.framework.data.commons.exception;

import com.tg.framework.commons.exception.NestedCodedRuntimeException;

public abstract class ModelException extends NestedCodedRuntimeException {

  private static final long serialVersionUID = 8738716899287151836L;

  public ModelException() {
    super();
  }

  public ModelException(String message) {
    super(message);
  }

  public ModelException(String message, Throwable cause) {
    super(message, cause);
  }

  public ModelException(Throwable cause) {
    super(cause);
  }

  public ModelException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
