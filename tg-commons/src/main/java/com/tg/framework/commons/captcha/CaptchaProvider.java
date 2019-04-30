package com.tg.framework.commons.captcha;

import com.tg.framework.commons.http.RequestDetails;

public interface CaptchaProvider {

  CaptchaValidator getCaptchaValidator(RequestDetails requestDetails);

}
