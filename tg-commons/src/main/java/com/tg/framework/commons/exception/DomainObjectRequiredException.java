package com.tg.framework.commons.exception;

public class DomainObjectRequiredException extends NestedCodedRuntimeException {

  private static final long serialVersionUID = -8408973709553472406L;

  public static final String PRESENT_CODE = "DO#Required";

  @Override
  protected String getPresentCode() {
    return PRESENT_CODE;
  }

  public DomainObjectRequiredException() {
    super();
  }

  public DomainObjectRequiredException(String message) {
    super(message);
  }

  public DomainObjectRequiredException(String message, Throwable cause) {
    super(message, cause);
  }

  public DomainObjectRequiredException(Throwable cause) {
    super(cause);
  }

  public DomainObjectRequiredException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
