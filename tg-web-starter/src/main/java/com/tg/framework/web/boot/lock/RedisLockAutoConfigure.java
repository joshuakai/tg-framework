package com.tg.framework.web.boot.lock;

import com.tg.framework.commons.concurrent.lock.redis.RedisLockAspect;
import com.tg.framework.commons.concurrent.lock.redis.RedisLockService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnClass({RedisTemplate.class})
@ConditionalOnProperty(prefix = "tg.redis-lock", value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(RedisLockProperties.class)
public class RedisLockAutoConfigure {

  @Bean
  @ConditionalOnMissingBean
  public RedisLockService redisLockService(RedisTemplate<String, Long> redisTemplate) {
    return new RedisLockService(redisTemplate);
  }

  @Bean
  @ConditionalOnMissingBean
  public RedisLockAspect redisLockAspect(RedisLockService redisLockService,
      RedisLockProperties redisLockProperties) {
    return new RedisLockAspect(redisLockService, redisLockProperties.getKeyPrefix(),
        redisLockProperties.getDefaultTimeoutMillis());
  }

}
