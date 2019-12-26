package com.tg.framework.web.captcha;

import com.tg.framework.commons.captcha.CaptchaVoter;
import javax.servlet.http.HttpServletRequest;

public interface CaptchaProvider {

  CaptchaVoter provide(HttpServletRequest request);

}
