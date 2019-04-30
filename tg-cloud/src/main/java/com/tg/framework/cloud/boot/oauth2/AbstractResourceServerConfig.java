package com.tg.framework.cloud.boot.oauth2;

import com.tg.framework.web.util.WebSecurityUtils;
import javax.annotation.Resource;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties(AuthorizationServerProperties.class)
public abstract class AbstractResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Resource
  private ResourceServerProperties resource;
  @Resource
  private OAuth2ClientProperties oAuth2ClientProperties;
  @Resource
  private AuthorizationServerProperties authorizationServerProperties;
  @Resource
  protected WebEndpointProperties webEndpointProperties;

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public RemoteTokenServices remoteTokenServices() {
    RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
    remoteTokenServices.setRestTemplate(restTemplate());
    remoteTokenServices.setClientId(oAuth2ClientProperties.getClientId());
    remoteTokenServices.setClientSecret(oAuth2ClientProperties.getClientSecret());
    remoteTokenServices
        .setCheckTokenEndpointUrl(authorizationServerProperties.getCheckTokenAccess());
    return remoteTokenServices;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().requestMatchers(ignoringRequestMatchers()).permitAll().anyRequest()
        .authenticated();
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId(resource.getResourceId()).tokenServices(remoteTokenServices());
  }

  protected RequestMatcher actuatorRequestMatcher() {
    return new AntPathRequestMatcher(
        WebSecurityUtils.antPatternOfBasePath(webEndpointProperties.getBasePath()),
        HttpMethod.GET.toString());
  }

  protected RequestMatcher[] ignoringRequestMatchers() throws Exception {
    return new RequestMatcher[]{actuatorRequestMatcher()};
  }

}
