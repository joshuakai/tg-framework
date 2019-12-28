package com.tg.framework.commons.exception;

public class TransactionalException extends NestedException {

  private static final long serialVersionUID = -4547974763082227859L;

  public TransactionalException(String code) {
    super(code);
  }

  public TransactionalException(String code, String message) {
    super(code, message);
  }

  public TransactionalException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }

  public TransactionalException(String code, Throwable cause) {
    super(code, cause);
  }

  public TransactionalException(String code, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(code, message, cause, enableSuppression, writableStackTrace);
  }

  public TransactionalException(String code, Object[] args) {
    super(code, args);
  }

  public TransactionalException(String code, Object[] args, String message) {
    super(code, args, message);
  }

  public TransactionalException(String code, Object[] args, String message, Throwable cause) {
    super(code, args, message, cause);
  }

  public TransactionalException(String code, Object[] args, Throwable cause) {
    super(code, args, cause);
  }

  public TransactionalException(String code, Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(code, args, message, cause, enableSuppression, writableStackTrace);
  }
}
