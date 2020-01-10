package com.tg.framework.web.boot.transaction;

import java.util.Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tg.transaction.interceptor")
public class TransactionInterceptorProperties {

  private String pointcut;
  private Properties attributes;

  public String getPointcut() {
    return pointcut;
  }

  public void setPointcut(String pointcut) {
    this.pointcut = pointcut;
  }

  public Properties getAttributes() {
    return attributes;
  }

  public void setAttributes(Properties attributes) {
    this.attributes = attributes;
  }
}
