package com.tg.framework.commons;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

public class FrameworkMessageSource extends ResourceBundleMessageSource {

  public FrameworkMessageSource() {
    setBasename("com.tg.framework.messages");
  }

  public static MessageSourceAccessor getAccessor() {
    return new MessageSourceAccessor(new FrameworkMessageSource());
  }

}
