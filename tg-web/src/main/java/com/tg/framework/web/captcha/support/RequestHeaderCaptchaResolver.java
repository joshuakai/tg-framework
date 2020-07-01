package com.tg.framework.web.captcha.support;

import com.tg.framework.web.captcha.CaptchaResolver;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;

public class RequestHeaderCaptchaResolver implements CaptchaResolver<HttpServletRequest> {

  private String headerName = "captcha";

  public RequestHeaderCaptchaResolver() {
  }

  public RequestHeaderCaptchaResolver(String headerName) {
    Assert.hasText(headerName, "A header name must be set");
    this.headerName = headerName;
  }

  @Override
  public String resolve(HttpServletRequest context) {
    return context.getHeader(headerName);
  }
}
