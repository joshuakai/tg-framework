package com.tg.framework.cloud.boot.oauth2;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

@EnableConfigurationProperties(AuthorizationServerProperties.class)
public abstract class AbstractResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Autowired(required = false)
  protected WebEndpointProperties webEndpointProperties;
  @Resource
  private ResourceServerProperties resource;
  @Resource
  private RemoteTokenServices remoteTokenServices;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    if (webEndpointProperties != null) {
      http.requestMatcher(new NegatedRequestMatcher(
          new AntPathRequestMatcher(webEndpointProperties.getBasePath() + "/**")))
          .authorizeRequests().anyRequest().authenticated();
    } else {
      super.configure(http);
    }
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId(resource.getResourceId()).tokenServices(remoteTokenServices);
  }

}
