package com.tg.framework.commons.captcha.support;

import com.tg.framework.commons.captcha.CaptchaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoopCaptchaValidator implements CaptchaValidator {

  private static Logger logger = LoggerFactory.getLogger(NoopCaptchaValidator.class);

  public static NoopCaptchaValidator getInstance() {
    return NoopCaptchaValidatorHolder.INSTANCE;
  }

  private static class NoopCaptchaValidatorHolder {

    private static final NoopCaptchaValidator INSTANCE = new NoopCaptchaValidator();
  }

  private NoopCaptchaValidator() {
  }

  @Override
  public final boolean validate(String captcha) {
    logger.debug("Validate captcha {}", captcha);
    return true;
  }

}
