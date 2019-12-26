package com.tg.framework.commons.captcha.support;

import com.tg.framework.commons.captcha.CaptchaVoter;

public class NoopCaptchaVoter implements CaptchaVoter {

  public static NoopCaptchaVoter getInstance() {
    return NoopCaptchaValidatorHolder.INSTANCE;
  }

  private static class NoopCaptchaValidatorHolder {

    private static final NoopCaptchaVoter INSTANCE = new NoopCaptchaVoter();
  }

  private NoopCaptchaVoter() {
  }

  @Override
  public final boolean vote(String captcha) {
    return true;
  }

}
