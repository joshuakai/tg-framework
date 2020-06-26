package com.tg.framework.commons.data;

import com.tg.framework.commons.NestedException;

public abstract class NestedTransactionException extends NestedException {

  private static final long serialVersionUID = -7974284841617234185L;

  public NestedTransactionException(String message) {
    super(message);
  }

  public NestedTransactionException(String message, boolean writableStackTrace) {
    super(message, writableStackTrace);
  }

  public NestedTransactionException(String message, Throwable cause) {
    super(message, cause);
  }
}
