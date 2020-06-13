package com.tg.framework.cloud.boot.http;

import com.tg.framework.commons.util.JSONUtils;
import com.tg.framework.web.boot.http.HttpClientAutoConfigure;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
@Import(HttpClientAutoConfigure.class)
@ConditionalOnClass({OAuth2RestTemplate.class, HttpClient.class, LoadBalanced.class})
@ConditionalOnProperty(prefix = "tg.cloud.oauth2-rest-template", value = "enabled")
@ConfigurationProperties(prefix = "security.oauth2.client")
public class OAuth2RestTemplateAutoConfigure {

  private String accessTokenUri;
  private String clientId;
  private String clientSecret;

  @Bean("oAuth2RestTemplate")
  @LoadBalanced
  @ConditionalOnMissingBean(name = "oAuth2RestTemplate")
  public OAuth2RestTemplate oAuth2RestTemplate(HttpClient httpClient) {
    ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
    details.setAccessTokenUri(accessTokenUri);
    details.setClientId(clientId);
    details.setClientSecret(clientSecret);
    OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(details);
    oAuth2RestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    oAuth2RestTemplate.getMessageConverters().stream()
        .filter(c -> c instanceof MappingJackson2HttpMessageConverter)
        .forEach(c -> ((MappingJackson2HttpMessageConverter) c)
            .setObjectMapper(JSONUtils.transferObjectMapper()));
    return oAuth2RestTemplate;
  }

  public String getAccessTokenUri() {
    return accessTokenUri;
  }

  public void setAccessTokenUri(String accessTokenUri) {
    this.accessTokenUri = accessTokenUri;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
}
