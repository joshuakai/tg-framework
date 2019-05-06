package com.tg.framework.commons.captcha.support;

import com.tg.framework.commons.captcha.CaptchaValidator;

public class NoopCaptchaValidator implements CaptchaValidator {

  public static NoopCaptchaValidator getInstance() {
    return NoopCaptchaValidatorHolder.INSTANCE;
  }

  private static class NoopCaptchaValidatorHolder {

    private static final NoopCaptchaValidator INSTANCE = new NoopCaptchaValidator();
  }

  private NoopCaptchaValidator() {
  }

  @Override
  public final boolean validate(String captcha) {
    return true;
  }

}
