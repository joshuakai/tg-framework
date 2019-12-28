package com.tg.framework.web.boot.cache;

import com.tg.framework.commons.util.JSONUtils;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching(proxyTargetClass = true)
@ConfigurationProperties("tg.cache")
public class DefaultCachingConfig extends CachingConfigurerSupport {

  @Resource
  private LettuceConnectionFactory lettuceConnectionFactory;

  private Long ttl = -1L;
  private Map<String, Long> initialTtl = new HashMap<>();

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
    SerializationPair<String> keySerializationPair = RedisSerializationContext.SerializationPair
        .fromSerializer(keySerializer());
    SerializationPair<?> valueSerializationPair = RedisSerializationContext.SerializationPair
        .fromSerializer(valueSerializer());
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(keySerializationPair)
        .serializeValuesWith(valueSerializationPair)
        .entryTtl(Duration.ofMillis(ttl));
    Map<String, RedisCacheConfiguration> initialCacheConfigurations = initialTtl.entrySet().stream()
        .collect(Collectors.toMap(
            Entry::getKey,
            e -> RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(keySerializationPair)
                .serializeValuesWith(valueSerializationPair)
                .entryTtl(Duration.ofMillis(e.getValue()))
        ));
    return RedisCacheManagerBuilder.fromConnectionFactory(lettuceConnectionFactory)
        .cacheDefaults(redisCacheConfiguration)
        .withInitialCacheConfigurations(initialCacheConfigurations).build();
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
  public RedisSerializer<String> keySerializer() {
    return new StringRedisSerializer();
  }

  @Bean
  public RedisSerializer<?> valueSerializer() {
    return new GenericJackson2JsonRedisSerializer(JSONUtils.serializationObjectMapper());
  }

  @Bean("jdkSerializationRedisTemplate")
  public RedisTemplate jdkSerializationRedisTemplate(
      LettuceConnectionFactory redisConnectionFactory) {
    StringRedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
    RedisSerializer keySerializer = keySerializer();
    redisTemplate.setKeySerializer(keySerializer);
    redisTemplate.setHashKeySerializer(keySerializer);
    RedisSerializer valueSerializer = jdkSerializationValueSerializer();
    redisTemplate.setValueSerializer(valueSerializer);
    redisTemplate.setHashValueSerializer(valueSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  @Bean("jdkSerializationValueSerializer")
  public RedisSerializer jdkSerializationValueSerializer() {
    return new JdkSerializationRedisSerializer();
  }

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
