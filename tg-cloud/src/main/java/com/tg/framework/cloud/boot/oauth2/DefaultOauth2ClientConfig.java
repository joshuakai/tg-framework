package com.tg.framework.cloud.boot.oauth2;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class DefaultOauth2ClientConfig {

  @Bean
  @LoadBalanced
  public OAuth2RestTemplate restTemplate(
      OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails,
      OAuth2ClientContext oauth2ClientContext) {
    return new OAuth2RestTemplate(oAuth2ProtectedResourceDetails, oauth2ClientContext);
  }

}
