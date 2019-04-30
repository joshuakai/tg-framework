package com.tg.framework.web.captcha;

import javax.servlet.http.HttpServletRequest;

public interface CaptchaArgumentResolver {

  String resolveArgument(HttpServletRequest request);

}
