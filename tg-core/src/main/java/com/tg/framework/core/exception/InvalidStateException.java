package com.tg.framework.core.exception;

public class InvalidStateException extends BusinessException {

  public static final String PRESENT_CODE = "DomainObject#InvalidState";

  private static final long serialVersionUID = 4558702588840671918L;

  public InvalidStateException(Object state) {
    super(PRESENT_CODE, new Object[]{state});
  }

  public InvalidStateException(Object state, String message) {
    super(PRESENT_CODE, new Object[]{state}, message);
  }

  public InvalidStateException(Object state, String message,
      Throwable cause) {
    super(PRESENT_CODE, new Object[]{state}, message, cause);
  }

  public InvalidStateException(Object state, Throwable cause) {
    super(PRESENT_CODE, new Object[]{state}, cause);
  }

  public InvalidStateException(Object state, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, new Object[]{state}, message, cause, enableSuppression, writableStackTrace);
  }
}
