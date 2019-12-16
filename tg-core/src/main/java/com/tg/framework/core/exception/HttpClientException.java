package com.tg.framework.core.exception;

public class HttpClientException extends NestedRuntimeException {

  private static final long serialVersionUID = -961770641625806479L;

  public static final String PRESENT_CODE = "Http";

  public HttpClientException() {
    super(PRESENT_CODE);
  }

  public HttpClientException(String message) {
    super(PRESENT_CODE, message);
  }

  public HttpClientException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public HttpClientException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public HttpClientException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public HttpClientException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public HttpClientException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public HttpClientException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public HttpClientException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public HttpClientException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }
}
