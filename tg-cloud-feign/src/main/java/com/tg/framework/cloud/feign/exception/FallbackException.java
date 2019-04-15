package com.tg.framework.cloud.feign.exception;

import com.tg.framework.core.exception.NestedRuntimeException;

public class FallbackException extends NestedRuntimeException {

  private static final long serialVersionUID = -1005218335884286307L;

  public FallbackException(String code) {
    super(code);
  }

  public FallbackException(String code, String message) {
    super(code, message);
  }

  public FallbackException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }

  public FallbackException(String code, Throwable cause) {
    super(code, cause);
  }

  public FallbackException(String code, String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(code, message, cause, enableSuppression, writableStackTrace);
  }

  public FallbackException(String code, Object[] args) {
    super(code, args);
  }

  public FallbackException(String code, Object[] args, String message) {
    super(code, args, message);
  }

  public FallbackException(String code, Object[] args, String message, Throwable cause) {
    super(code, args, message, cause);
  }

  public FallbackException(String code, Object[] args, Throwable cause) {
    super(code, args, cause);
  }

  public FallbackException(String code, Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(code, args, message, cause, enableSuppression, writableStackTrace);
  }

}
