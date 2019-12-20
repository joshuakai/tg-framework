package com.tg.framework.web.captcha.support;

import com.tg.framework.beans.captcha.CaptchaType;
import com.tg.framework.web.captcha.CaptchaFailureHandler;
import com.tg.framework.web.captcha.exception.InvalidCaptchaException;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

public class DefaultCaptchaFailureHandler implements CaptchaFailureHandler {

  @Override
  public void onCaptchaFailure(HttpServletRequest request, HttpServletResponse response,
      CaptchaType captchaType) throws IOException, ServletException {
    throw new InvalidCaptchaException(
        Optional.ofNullable(captchaType).map(CaptchaType::name).orElse(StringUtils.EMPTY));
  }
}
