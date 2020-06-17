package com.tg.framework.commons.data;

public class TransactionException extends NestedTransactionException {

  private static final long serialVersionUID = -8115154731427188241L;

  public TransactionException(String message) {
    super(message);
  }

  public TransactionException(String message, boolean writableStackTrace) {
    super(message, writableStackTrace);
  }

  public TransactionException(String message, Throwable cause) {
    super(message, cause);
  }
}
