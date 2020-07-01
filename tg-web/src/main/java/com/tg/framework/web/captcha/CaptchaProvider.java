package com.tg.framework.web.captcha;

import com.tg.framework.commons.captcha.CaptchaVoter;

public interface CaptchaProvider<T> {

  CaptchaVoter provide(T context);

}
