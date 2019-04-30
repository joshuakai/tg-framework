package com.tg.framework.web.boot.cache;

import com.tg.framework.commons.util.JSONUtils;
import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching(proxyTargetClass = true)
public class DefaultCachingConfig extends CachingConfigurerSupport {

  @Resource
  private LettuceConnectionFactory lettuceConnectionFactory;

  @Bean
  @Override
  public KeyGenerator keyGenerator() {
    return (target, method, params) -> {
      StringBuilder sb = new StringBuilder().append(target.getClass().getName())
          .append(method.getName());
      Optional.ofNullable(params).ifPresent(ps -> Stream.of(ps).forEach(p -> sb.append(p)));
      return sb.toString();
    };
  }

  @Bean
  @Override
  public CacheManager cacheManager() {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
        .entryTtl(Duration.ofNanos(-1));
    return RedisCacheManagerBuilder.fromConnectionFactory(lettuceConnectionFactory)
        .cacheDefaults(redisCacheConfiguration).build();
  }

  @Bean
  public RedisTemplate redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
    StringRedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
    RedisSerializer keySerializer = keySerializer();
    redisTemplate.setKeySerializer(keySerializer);
    redisTemplate.setHashKeySerializer(keySerializer);
    RedisSerializer valueSerializer = valueSerializer();
    redisTemplate.setValueSerializer(valueSerializer);
    redisTemplate.setHashValueSerializer(valueSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  @Bean
  public RedisSerializer keySerializer() {
    return new StringRedisSerializer();
  }

  @Bean
  public RedisSerializer valueSerializer() {
    return new GenericJackson2JsonRedisSerializer(JSONUtils.serializationObjectMapper());
  }

}
