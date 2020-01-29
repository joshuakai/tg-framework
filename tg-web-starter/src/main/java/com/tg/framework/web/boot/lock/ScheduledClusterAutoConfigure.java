package com.tg.framework.web.boot.lock;

import com.tg.framework.commons.concurrent.lock.IdentityLock;
import com.tg.framework.commons.concurrent.lock.redis.RedisLockService;
import com.tg.framework.commons.concurrent.lock.scheduling.ScheduledClusterAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnClass({RedisTemplate.class})
@ConditionalOnProperty(prefix = "tg.scheduled-cluster", value = "enabled")
@ConfigurationProperties("tg.scheduled-cluster")
public class ScheduledClusterAutoConfigure {

  private String keyPrefix = "schedules:";

  @Bean
  @ConditionalOnMissingBean
  public RedisLockService redisLockService(RedisTemplate<String, IdentityLock> redisTemplate) {
    return new RedisLockService(redisTemplate);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduledClusterAspect scheduledClusterAspect(RedisLockService redisLockService) {
    return new ScheduledClusterAspect(redisLockService, keyPrefix);
  }

  public String getKeyPrefix() {
    return keyPrefix;
  }

  public void setKeyPrefix(String keyPrefix) {
    this.keyPrefix = keyPrefix;
  }
}
