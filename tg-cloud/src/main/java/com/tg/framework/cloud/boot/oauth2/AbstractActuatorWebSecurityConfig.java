package com.tg.framework.cloud.boot.oauth2;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public abstract class AbstractActuatorWebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired(required = false)
  protected WebEndpointProperties webEndpointProperties;

  @Override
  public void configure(WebSecurity web) throws Exception {
    Optional.ofNullable(webEndpointProperties).map(WebEndpointProperties::getBasePath)
        .map(basePath -> new AntPathRequestMatcher(basePath + "/**"))
        .ifPresent(web.ignoring()::requestMatchers);
    web.ignoring().antMatchers("/error");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().authenticated();
  }

}
