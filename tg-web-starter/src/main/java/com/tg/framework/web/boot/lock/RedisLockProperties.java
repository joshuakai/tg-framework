package com.tg.framework.web.boot.lock;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tg.redis-lock")
public class RedisLockProperties {

  private String keyPrefix = "locks:";
  private long defaultTimeoutMillis = -1L;

  public String getKeyPrefix() {
    return keyPrefix;
  }

  public void setKeyPrefix(String keyPrefix) {
    this.keyPrefix = keyPrefix;
  }

  public long getDefaultTimeoutMillis() {
    return defaultTimeoutMillis;
  }

  public void setDefaultTimeoutMillis(long defaultTimeoutMillis) {
    this.defaultTimeoutMillis = defaultTimeoutMillis;
  }
}
