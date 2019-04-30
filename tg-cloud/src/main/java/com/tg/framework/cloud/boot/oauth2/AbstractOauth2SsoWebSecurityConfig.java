package com.tg.framework.cloud.boot.oauth2;

import com.tg.framework.web.util.WebSecurityUtils;
import javax.annotation.Resource;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AbstractOauth2SsoWebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Resource
  protected WebEndpointProperties webEndpointProperties;

  protected RequestMatcher actuatorRequestMatcher() {
    return new AntPathRequestMatcher(
        WebSecurityUtils.antPatternOfBasePath(webEndpointProperties.getBasePath()),
        HttpMethod.GET.toString());
  }

  protected RequestMatcher[] ignoringRequestMatchers() {
    return new RequestMatcher[]{actuatorRequestMatcher()};
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().requestMatchers(ignoringRequestMatchers()).permitAll().anyRequest()
        .authenticated();
  }

  @Bean
  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }
}
