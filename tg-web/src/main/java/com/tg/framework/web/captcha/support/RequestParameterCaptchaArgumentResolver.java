package com.tg.framework.web.captcha.support;

import com.tg.framework.web.captcha.CaptchaArgumentResolver;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;

public class RequestParameterCaptchaArgumentResolver implements CaptchaArgumentResolver {

  private String parameterName = "captcha";

  public RequestParameterCaptchaArgumentResolver() {
  }

  public RequestParameterCaptchaArgumentResolver(String parameterName) {
    Assert.notNull(parameterName, "Parameter name must not be null.");
    this.parameterName = parameterName;
  }

  @Override
  public String resolveArgument(HttpServletRequest request) {
    return request.getParameter(parameterName);
  }
}
