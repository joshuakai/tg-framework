package com.tg.framework.web.captcha;

import com.tg.framework.beans.captcha.CaptchaType;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface CaptchaFailureHandler {

  void onCaptchaFailure(HttpServletRequest request, HttpServletResponse response, CaptchaType captchaType) throws IOException, ServletException;

}
