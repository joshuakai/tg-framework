package com.tg.framework.web.captcha.support;

import com.tg.framework.web.captcha.CaptchaResolver;

public class NoopCaptchaResolver implements CaptchaResolver<Object> {

  public static NoopCaptchaResolver getInstance() {
    return NoopCaptchaArgumentResolverHolder.INSTANCE;
  }

  private static class NoopCaptchaArgumentResolverHolder {

    private static final NoopCaptchaResolver INSTANCE = new NoopCaptchaResolver();
  }

  private NoopCaptchaResolver() {
  }

  @Override
  public String resolve(Object context) {
    return null;
  }
}
