package com.tg.framework.commons.exception;

public class ResourceNotFoundException extends NestedCodedRuntimeException {

  private static final long serialVersionUID = -6098581242915032532L;

  public static final String PRESENT_CODE = "Resource#NotFound";

  private String resource;

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public ResourceNotFoundException(String resource) {
    super();
    this.resource = resource;
  }

  public ResourceNotFoundException(String message, String resource) {
    super(message);
    this.resource = resource;
  }

  public ResourceNotFoundException(String message, Throwable cause, String resource) {
    super(message, cause);
    this.resource = resource;
  }

  public ResourceNotFoundException(Throwable cause, String resource) {
    super(cause);
    this.resource = resource;
  }

  public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String resource) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.resource = resource;
  }

  public String getResource() {
    return resource;
  }
}
