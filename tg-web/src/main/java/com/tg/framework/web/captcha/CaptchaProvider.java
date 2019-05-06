package com.tg.framework.web.captcha;

import com.tg.framework.commons.captcha.CaptchaValidator;
import javax.servlet.http.HttpServletRequest;

public interface CaptchaProvider {

  CaptchaValidator provide(HttpServletRequest request);

}
