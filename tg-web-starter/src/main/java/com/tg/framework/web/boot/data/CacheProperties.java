package com.tg.framework.web.boot.data;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tg.cache")
public class CacheProperties {

  private Long ttl = -1L;
  private Map<String, Long> initialTtl = new HashMap<>();

  public Long getTtl() {
    return ttl;
  }

  public void setTtl(Long ttl) {
    this.ttl = ttl;
  }

  public Map<String, Long> getInitialTtl() {
    return initialTtl;
  }

  public void setInitialTtl(Map<String, Long> initialTtl) {
    this.initialTtl = initialTtl;
  }
}
