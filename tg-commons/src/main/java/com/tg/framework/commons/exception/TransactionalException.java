package com.tg.framework.commons.exception;

public abstract class TransactionalException extends NestedException {

  private static final long serialVersionUID = -4547974763082227859L;

  public TransactionalException() {
  }

  public TransactionalException(String message) {
    super(message);
  }

  public TransactionalException(String message, Throwable cause) {
    super(message, cause);
  }

  public TransactionalException(Throwable cause) {
    super(cause);
  }

  public TransactionalException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
