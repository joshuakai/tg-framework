package com.tg.framework.web.captcha.support;

import com.tg.framework.beans.captcha.CaptchaType;
import com.tg.framework.web.captcha.CaptchaFailureHandler;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public class DefaultCaptchaFailureHandler implements CaptchaFailureHandler {

  @Override
  public boolean onCaptchaFailure(HttpServletRequest request, HttpServletResponse response,
      CaptchaType captchaType) throws IOException {
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    response.getWriter().print("Bad captcha.");
    response.flushBuffer();
    return false;
  }
}
