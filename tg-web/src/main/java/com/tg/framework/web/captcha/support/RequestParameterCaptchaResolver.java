package com.tg.framework.web.captcha.support;

import com.tg.framework.web.captcha.CaptchaResolver;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;

public class RequestParameterCaptchaResolver implements CaptchaResolver<HttpServletRequest> {

  private String parameterName = "captcha";

  public RequestParameterCaptchaResolver() {
  }

  public RequestParameterCaptchaResolver(String parameterName) {
    Assert.hasText(parameterName, "A parameter name must be set");
    this.parameterName = parameterName;
  }

  @Override
  public String resolve(HttpServletRequest context) {
    return context.getParameter(parameterName);
  }
}
