package com.tg.framework.commons.http.exception;

import com.tg.framework.commons.exception.NestedCodedRuntimeException;

public class RequestHeaderRequiredException extends NestedCodedRuntimeException {

  private static final long serialVersionUID = -961770641625806479L;

  public static final String PRESENT_CODE = "Http#HeaderRequired";

  private String headerName;

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public RequestHeaderRequiredException(String headerName) {
    super();
    this.headerName = headerName;
  }

  public RequestHeaderRequiredException(String message, String headerName) {
    super(message);
    this.headerName = headerName;
  }

  public RequestHeaderRequiredException(String message, Throwable cause, String headerName) {
    super(message, cause);
    this.headerName = headerName;
  }

  public RequestHeaderRequiredException(Throwable cause, String headerName) {
    super(cause);
    this.headerName = headerName;
  }

  public RequestHeaderRequiredException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String headerName) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.headerName = headerName;
  }

  public String getHeaderName() {
    return headerName;
  }
}
