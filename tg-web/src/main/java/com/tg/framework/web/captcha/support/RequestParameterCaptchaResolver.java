package com.tg.framework.web.captcha.support;

import com.tg.framework.web.captcha.CaptchaResolver;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;

public class RequestParameterCaptchaResolver implements CaptchaResolver {

  private String parameterName = "captcha";

  public RequestParameterCaptchaResolver() {
  }

  public RequestParameterCaptchaResolver(String parameterName) {
    Assert.hasText(parameterName, "Parameter name must not be null or empty");
    this.parameterName = parameterName;
  }

  @Override
  public String resolve(HttpServletRequest request) {
    return request.getParameter(parameterName);
  }
}
