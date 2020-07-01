package com.tg.framework.web.captcha;

@FunctionalInterface
public interface CaptchaResolver<T> {

  String resolve(T context);

}
