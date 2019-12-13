package com.tg.framework.web.boot.cache;

import com.tg.framework.core.concurrent.lock.redis.RedisLockAspect;
import com.tg.framework.core.concurrent.lock.redis.RedisLockService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConfigurationProperties("tg.redis.lock")
public class DefaultRedisLockConfig {

  private String keyPrefix = "locks:";
  private long defaultTimeoutMillis = -1L;

  @Bean
  public RedisLockService redisLockService(RedisTemplate<String, Long> redisTemplate) {
    return new RedisLockService(redisTemplate);
  }

  @Bean
  public RedisLockAspect redisLockAspect(RedisLockService redisLockService) {
    return new RedisLockAspect(redisLockService, keyPrefix, defaultTimeoutMillis);
  }

}
