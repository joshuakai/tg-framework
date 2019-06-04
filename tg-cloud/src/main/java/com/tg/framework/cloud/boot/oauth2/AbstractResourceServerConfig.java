package com.tg.framework.cloud.boot.oauth2;

import java.io.IOException;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties(AuthorizationServerProperties.class)
public abstract class AbstractResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Autowired(required = false)
  protected WebEndpointProperties webEndpointProperties;
  @Resource
  private ResourceServerProperties resource;
  @Resource
  private OAuth2ClientProperties oAuth2ClientProperties;
  @Resource
  private AuthorizationServerProperties authorizationServerProperties;

  @Bean("remoteTokenRestTemplate")
  @LoadBalanced
  public RestTemplate remoteTokenRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
      @Override
      // Ignore 400
      public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getRawStatusCode() != 400) {
          super.handleError(response);
        }
      }
    });
    return restTemplate;
  }

  @Bean
  public RemoteTokenServices remoteTokenServices() {
    RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
    remoteTokenServices.setRestTemplate(remoteTokenRestTemplate());
    remoteTokenServices.setClientId(oAuth2ClientProperties.getClientId());
    remoteTokenServices.setClientSecret(oAuth2ClientProperties.getClientSecret());
    remoteTokenServices
        .setCheckTokenEndpointUrl(authorizationServerProperties.getCheckTokenAccess());
    return remoteTokenServices;
  }

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
    resources.resourceId(resource.getResourceId()).tokenServices(remoteTokenServices());
  }

}
