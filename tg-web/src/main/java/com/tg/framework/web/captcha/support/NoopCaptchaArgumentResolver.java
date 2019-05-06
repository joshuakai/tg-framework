package com.tg.framework.web.captcha.support;

import com.tg.framework.web.captcha.CaptchaArgumentResolver;
import javax.servlet.http.HttpServletRequest;

public class NoopCaptchaArgumentResolver implements CaptchaArgumentResolver {

  public static NoopCaptchaArgumentResolver getInstance() {
    return NoopCaptchaArgumentResolverHolder.INSTANCE;
  }

  private static class NoopCaptchaArgumentResolverHolder {

    private static final NoopCaptchaArgumentResolver INSTANCE = new NoopCaptchaArgumentResolver();
  }

  private NoopCaptchaArgumentResolver() {
  }

  @Override
  public String resolveArgument(HttpServletRequest request) {
    return null;
  }
}
