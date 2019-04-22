package com.tg.framework.cloud.boot.oauth2;

import com.tg.framework.web.util.WebSecurityUtils;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public abstract class AbstractResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Value("${spring.application.name}")
  private String resourceId;

  @Resource
  protected WebEndpointProperties webEndpointProperties;

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
    return defaultTokenServices;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().requestMatchers(ignoringRequestMatchers()).permitAll().anyRequest()
        .authenticated();
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId(resourceId()).tokenServices(tokenServices());
  }

  protected String resourceId() {
    return resourceId;
  }

  protected RequestMatcher[] ignoringRequestMatchers() throws Exception {
    return new RequestMatcher[]{new AntPathRequestMatcher(
        WebSecurityUtils.antPatternOfBasePath(webEndpointProperties.getBasePath()),
        HttpMethod.GET.toString())};
  }

}
