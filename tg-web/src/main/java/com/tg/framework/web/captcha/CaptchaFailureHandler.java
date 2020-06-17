package com.tg.framework.web.captcha;

import com.tg.framework.commons.captcha.CaptchaType;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

@FunctionalInterface
public interface CaptchaFailureHandler {

  boolean onCaptchaFailure(HttpServletRequest request, HttpServletResponse response, CaptchaType captchaType)
      throws IOException, ServletException;

  CaptchaFailureHandler DEFAULT = (request, response, captchaType) -> {
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    response.getWriter().write("Bad captcha");
    response.flushBuffer();
    return false;
  };

}
