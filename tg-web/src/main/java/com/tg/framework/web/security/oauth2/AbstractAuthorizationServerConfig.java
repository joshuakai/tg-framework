package com.tg.framework.web.security.oauth2;

import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@SuppressWarnings("unchecked")
public abstract class AbstractAuthorizationServerConfig extends
    AuthorizationServerConfigurerAdapter {

  @Resource
  protected RedisConnectionFactory redisConnectionFactory;

  @Bean
  @Primary
  public TokenStore tokenStore() {
    return new RedisTokenStore(redisConnectionFactory);
  }

  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setSupportRefreshToken(true);
    return defaultTokenServices;
  }

  @Bean
  public AuthorizationCodeServices authorizationCodeServices(
      RedisTemplate<String, OAuth2Authentication> redisTemplate) {
    return new RandomValueAuthorizationCodeServices() {

      private static final String REDIS_KEY_TEMPLATE = "authorization_code_to_access:%s";

      @Override
      protected void store(String code, OAuth2Authentication authentication) {
        redisTemplate.boundValueOps(String.format(REDIS_KEY_TEMPLATE, code))
            .set(authentication, 1, TimeUnit.MINUTES);
      }

      @Override
      protected OAuth2Authentication remove(String code) {
        OAuth2Authentication authentication = redisTemplate
            .boundValueOps(String.format(REDIS_KEY_TEMPLATE, code)).get();
        redisTemplate.delete(String.format(REDIS_KEY_TEMPLATE, code));
        return authentication;
      }
    };
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.checkTokenAccess("isAuthenticated()").tokenKeyAccess("permitAll()");
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager());
    endpoints.tokenStore(tokenStore());
    endpoints.authorizationCodeServices(
        authorizationCodeServices(authorizationCodeServiceRedisTemplate()));
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    super.configure(clients);
  }

  protected AuthenticationManager authenticationManager() {
    return null;
  }

  protected RedisTemplate authorizationCodeServiceRedisTemplate() {
    StringRedisSerializer keySerializer = new StringRedisSerializer();
    JdkSerializationRedisSerializer valueSerializer = new JdkSerializationRedisSerializer();
    StringRedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
    redisTemplate.setKeySerializer(keySerializer);
    redisTemplate.setHashKeySerializer(keySerializer);
    redisTemplate.setValueSerializer(valueSerializer);
    redisTemplate.setHashValueSerializer(valueSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}
