package com.tg.framework.core.exception;

import java.io.Serializable;

public class InvalidStateException extends BusinessException {

  public static final String PRESENT_CODE = "Biz#InvalidState";

  private static final long serialVersionUID = 4558702588840671918L;

  public InvalidStateException(Serializable state) {
    super(PRESENT_CODE, new Object[]{state});
  }

  public InvalidStateException(Serializable state, String message) {
    super(PRESENT_CODE, new Object[]{state}, message);
  }

  public InvalidStateException(Serializable state, String message,
      Throwable cause) {
    super(PRESENT_CODE, new Object[]{state}, message, cause);
  }

  public InvalidStateException(Serializable state, Throwable cause) {
    super(PRESENT_CODE, new Object[]{state}, cause);
  }

  public InvalidStateException(Serializable state, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, new Object[]{state}, message, cause, enableSuppression, writableStackTrace);
  }
}
