package com.tg.framework.cloud.boot.http;

import com.tg.framework.commons.util.JSONUtils;
import com.tg.framework.web.boot.http.HttpClientAutoConfigure;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import(HttpClientAutoConfigure.class)
@ConditionalOnProperty(prefix = "tg.cloud.rest-template", value = "enabled")
public class CloudRestTemplateAutoConfigure {

  @LoadBalanced
  @Bean("cloudRestTemplate")
  public RestTemplate cloudRestTemplate(HttpClient httpClient) {
    RestTemplate restTemplate = new RestTemplate(
        new HttpComponentsClientHttpRequestFactory(httpClient));
    restTemplate.getMessageConverters().stream()
        .filter(c -> c instanceof MappingJackson2HttpMessageConverter)
        .forEach(c -> ((MappingJackson2HttpMessageConverter) c)
            .setObjectMapper(JSONUtils.transferObjectMapper()));
    return restTemplate;
  }

}
