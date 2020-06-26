package com.tg.framework.web.captcha.support;

import com.tg.framework.web.captcha.CaptchaArgumentResolver;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;

public class RequestHeaderCaptchaArgumentResolver implements CaptchaArgumentResolver {

  private String headerName = "captcha";

  public RequestHeaderCaptchaArgumentResolver() {
  }

  public RequestHeaderCaptchaArgumentResolver(String headerName) {
    Assert.hasText(headerName, "Header name must not be null or empty");
    this.headerName = headerName;
  }

  @Override
  public String resolveArgument(HttpServletRequest request) {
    return request.getHeader(headerName);
  }
}
