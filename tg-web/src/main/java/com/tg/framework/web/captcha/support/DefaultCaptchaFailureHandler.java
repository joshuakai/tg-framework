package com.tg.framework.web.captcha.support;

import com.tg.framework.web.captcha.CaptchaFailureHandler;
import com.tg.framework.web.captcha.exception.InvalidCaptchaException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultCaptchaFailureHandler implements CaptchaFailureHandler {

  @Override
  public void onCaptchaFailure(HttpServletRequest request, HttpServletResponse response,
      InvalidCaptchaException exception) throws IOException, ServletException {
    throw exception;
  }
}
