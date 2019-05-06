package com.tg.framework.cloud.boot.oauth2;

import com.tg.framework.cloud.boot.util.ActuatorUtils;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public abstract class AbstractActuatorWebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired(required = false)
  protected WebEndpointProperties webEndpointProperties;

  @Override
  public void configure(WebSecurity web) throws Exception {
    Optional.ofNullable(webEndpointProperties).map(WebEndpointProperties::getBasePath)
        .map(ActuatorUtils::defaultExpose).ifPresent(web.ignoring()::requestMatchers);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().authenticated();
  }

}
