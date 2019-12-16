package com.tg.framework.web.captcha.exception;

import com.tg.framework.core.exception.ParameterException;

public class InvalidCaptchaException extends ParameterException {

  public static final String PRESENT_CODE = "Param#CaptchaInvalid";

  public InvalidCaptchaException() {
    super(PRESENT_CODE);
  }

  public InvalidCaptchaException(String message) {
    super(PRESENT_CODE, message);
  }

  public InvalidCaptchaException(String message, Throwable cause) {
    super(PRESENT_CODE, message, cause);
  }

  public InvalidCaptchaException(Throwable cause) {
    super(PRESENT_CODE, cause);
  }

  public InvalidCaptchaException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(PRESENT_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  public InvalidCaptchaException(Object[] args) {
    super(PRESENT_CODE, args);
  }

  public InvalidCaptchaException(Object[] args, String message) {
    super(PRESENT_CODE, args, message);
  }

  public InvalidCaptchaException(Object[] args, String message,
      Throwable cause) {
    super(PRESENT_CODE, args, message, cause);
  }

  public InvalidCaptchaException(Object[] args, Throwable cause) {
    super(PRESENT_CODE, args, cause);
  }

  public InvalidCaptchaException(Object[] args, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, args, message, cause, enableSuppression, writableStackTrace);
  }
}
