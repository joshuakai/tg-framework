package com.tg.framework.web.captcha.support;

import com.tg.framework.web.captcha.CaptchaResolver;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;

public class RequestHeaderCaptchaResolver implements CaptchaResolver {

  private String headerName = "captcha";

  public RequestHeaderCaptchaResolver() {
  }

  public RequestHeaderCaptchaResolver(String headerName) {
    Assert.hasText(headerName, "Header name must not be null or empty");
    this.headerName = headerName;
  }

  @Override
  public String resolve(HttpServletRequest request) {
    return request.getHeader(headerName);
  }
}
