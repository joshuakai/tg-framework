package com.tg.framework.core.exception;

public class AuthorityRequiredException extends NestedRuntimeException {

  private static final long serialVersionUID = 3758611621914546504L;

  public static final String PRESENT_CODE = "Security#AuthorityRequired";

  public AuthorityRequiredException() {
    super(PRESENT_CODE);
  }

  public AuthorityRequiredException(String message) {
    super(PRESENT_CODE, message);
  }

  public AuthorityRequiredException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public AuthorityRequiredException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public AuthorityRequiredException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public AuthorityRequiredException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public AuthorityRequiredException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public AuthorityRequiredException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public AuthorityRequiredException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public AuthorityRequiredException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }
}
