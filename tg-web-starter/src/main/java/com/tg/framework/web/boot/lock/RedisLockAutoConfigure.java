package com.tg.framework.web.boot.lock;

import com.tg.framework.commons.concurrent.lock.IdentityLock;
import com.tg.framework.commons.concurrent.lock.redis.RedisLockAspect;
import com.tg.framework.commons.concurrent.lock.redis.RedisLockService;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${spring.application.name:}")
  private String applicationName;
  @Value("${spring.cloud.consul.discovery.instance-id:}")
  private String consulInstanceId;

  @Bean
  @ConditionalOnMissingBean
  public RedisLockService redisLockService(RedisTemplate<String, IdentityLock> redisTemplate,
      RedisLockProperties redisLockProperties) {
    RedisLockService redisLockService = new RedisLockService(redisTemplate);
    redisLockService.setInstanceId(resolveInstanceId(redisLockProperties.getInstanceId()));
    return redisLockService;
  }

  private String resolveInstanceId(String instanceId) {
    return Stream.of(instanceId, consulInstanceId, applicationName).filter(StringUtils::isNotEmpty)
        .findFirst().orElse(null);
  }

  @Bean
  @ConditionalOnMissingBean
  public RedisLockAspect redisLockAspect(RedisLockService redisLockService,
      RedisLockProperties redisLockProperties) {
    return new RedisLockAspect(redisLockService, redisLockProperties.getKeyPrefix(),
        redisLockProperties.getDefaultTimeoutMillis());
  }

}
