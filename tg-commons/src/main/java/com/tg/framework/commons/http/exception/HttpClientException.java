package com.tg.framework.commons.http.exception;

import com.tg.framework.commons.exception.NestedCodedRuntimeException;

public class HttpClientException extends NestedCodedRuntimeException {

  private static final long serialVersionUID = -961770641625806479L;

  public static final String PRESENT_CODE = "Http#Client";

  private int statusCode;

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public HttpClientException() {
    this((String) null, 400);
  }

  public HttpClientException(int statusCode) {
    this((String) null, statusCode);
  }

  public HttpClientException(String message) {
    this(message, 400);
  }

  public HttpClientException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public HttpClientException(String message, Throwable cause, int statusCode) {
    super(message, cause);
    this.statusCode = statusCode;
  }

  public HttpClientException(Throwable cause, int statusCode) {
    super(cause);
    this.statusCode = statusCode;
  }

  public HttpClientException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, int statusCode) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
