package com.tg.framework.web.boot.task;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tg.redis-lock")
public class RedisMutexTaskProperties {

  private String instanceId;
  private String keyPrefix = "mutex_tasks:";

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public String getKeyPrefix() {
    return keyPrefix;
  }

  public void setKeyPrefix(String keyPrefix) {
    this.keyPrefix = keyPrefix;
  }

}
