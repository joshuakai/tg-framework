package com.tg.framework.web.captcha.support;

import com.tg.framework.web.captcha.CaptchaResolver;
import javax.servlet.http.HttpServletRequest;

public class NoopCaptchaResolver implements CaptchaResolver {

  public static NoopCaptchaResolver getInstance() {
    return NoopCaptchaArgumentResolverHolder.INSTANCE;
  }

  private static class NoopCaptchaArgumentResolverHolder {

    private static final NoopCaptchaResolver INSTANCE = new NoopCaptchaResolver();
  }

  private NoopCaptchaResolver() {
  }

  @Override
  public String resolve(HttpServletRequest request) {
    return null;
  }
}
