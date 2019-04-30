package com.tg.framework.commons.captcha.support;

import com.tg.framework.commons.captcha.CaptchaValidator;
import com.tg.framework.commons.lang.StringOptional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoogleAuthenticatorCaptchaValidator implements CaptchaValidator {

  private static Logger logger = LoggerFactory.getLogger(GoogleAuthenticatorCaptchaValidator.class);

  @Override
  public boolean validate(String captcha) {
    logger.debug("Validate captcha {}", captcha);
    return StringOptional.ofNullable(captcha).isPresent();
  }
}
