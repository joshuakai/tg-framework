package com.tg.framework.web.captcha;

import javax.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface CaptchaResolver {

  String resolve(HttpServletRequest request);

}
