package com.tg.framework.web.boot.cache;

import com.tg.framework.commons.util.JSONUtils;
import com.tg.framework.commons.util.StreamUtils;
import io.lettuce.core.RedisClient;
import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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
@ConditionalOnClass({CacheManager.class, RedisClient.class, RedisTemplate.class})
@ConditionalOnProperty(prefix = "tg.cache", value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfigure extends CachingConfigurerSupport {

  @Resource
  private LettuceConnectionFactory lettuceConnectionFactory;
  @Resource
  private CacheProperties cacheProperties;

  private RedisSerializer<String> keySerializer = new StringRedisSerializer();
  private RedisSerializer<?> valueSerializer = new GenericJackson2JsonRedisSerializer(
      JSONUtils.serializationObjectMapper());
  private RedisSerializer<?> jdkSerializationValueSerializer = new JdkSerializationRedisSerializer();

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
  @ConditionalOnMissingBean
  @Override
  public CacheManager cacheManager() {
    SerializationPair<String> keySerializationPair = RedisSerializationContext.SerializationPair
        .fromSerializer(keySerializer);
    SerializationPair<?> valueSerializationPair = RedisSerializationContext.SerializationPair
        .fromSerializer(valueSerializer);
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(keySerializationPair)
        .serializeValuesWith(valueSerializationPair)
        .entryTtl(Duration.ofMillis(cacheProperties.getTtl()));
    Map<String, RedisCacheConfiguration> initialCacheConfigurations = StreamUtils
        .safety2Stream(cacheProperties.getInitialTtl())
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
  @ConditionalOnMissingBean
  public RedisTemplate redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
    return buildRedisTemplate(redisConnectionFactory, keySerializer, valueSerializer);
  }

  @Bean("jdkSerializationRedisTemplate")
  @ConditionalOnMissingBean(name = "jdkSerializationRedisTemplate")
  public RedisTemplate jdkSerializationRedisTemplate(
      LettuceConnectionFactory redisConnectionFactory) {
    return buildRedisTemplate(redisConnectionFactory, keySerializer,
        jdkSerializationValueSerializer);
  }

  private static RedisTemplate buildRedisTemplate(RedisConnectionFactory redisConnectionFactory,
      RedisSerializer<?> keySerializer, RedisSerializer<?> valueSerializer) {
    StringRedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
    redisTemplate.setKeySerializer(keySerializer);
    redisTemplate.setHashKeySerializer(keySerializer);
    redisTemplate.setValueSerializer(valueSerializer);
    redisTemplate.setHashValueSerializer(valueSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }


}
