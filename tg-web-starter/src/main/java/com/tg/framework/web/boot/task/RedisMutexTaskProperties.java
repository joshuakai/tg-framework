package com.tg.framework.web.boot.task;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tg.redis-lock")
public class RedisMutexTaskProperties {

  private String keyPrefix = "mutex_tasks:";

  public String getKeyPrefix() {
    return keyPrefix;
  }

  public void setKeyPrefix(String keyPrefix) {
    this.keyPrefix = keyPrefix;
  }

}
