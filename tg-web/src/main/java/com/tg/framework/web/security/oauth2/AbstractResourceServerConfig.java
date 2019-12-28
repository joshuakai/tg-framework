package com.tg.framework.web.security.oauth2;

import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@EnableConfigurationProperties(AuthorizationServerProperties.class)
public abstract class AbstractResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Resource
  private ResourceServerProperties resource;
  @Resource
  private RemoteTokenServices remoteTokenServices;

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId(resource.getResourceId()).tokenServices(remoteTokenServices);
  }

}
