package com.tg.framework.web.captcha.support;

import com.tg.framework.commons.validation.Matchable;

public enum CaptchaArgumentType implements Matchable<String> {

  PARAMETER, HEADER;

  @Override
  public String getValue() {
    return name();
  }

}
