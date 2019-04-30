package com.tg.framework.commons.captcha.support;

import com.tg.framework.commons.validation.Matchable;

public enum CaptchaType implements Matchable<String> {

  NOOP, TENCENT, GOOGLE_AUTHENTICATOR;

  @Override
  public String getValue() {
    return name();
  }
}
