package com.tg.framework.web.captcha;

import com.tg.framework.web.captcha.exception.InvalidCaptchaException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CaptchaFailureHandler {

  void onCaptchaFailure(HttpServletRequest request, HttpServletResponse response,
      InvalidCaptchaException exception) throws IOException, ServletException;

}
