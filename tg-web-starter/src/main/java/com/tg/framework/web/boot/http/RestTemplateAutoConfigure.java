package com.tg.framework.web.boot.http;

import com.tg.framework.commons.util.JSONUtils;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import(HttpClientAutoConfigure.class)
@ConditionalOnClass({RestTemplate.class, HttpClient.class})
@ConditionalOnProperty(prefix = "tg.web.rest-template", value = "enabled")
public class RestTemplateAutoConfigure {

  @Bean("defaultRestTemplate")
  @ConditionalOnMissingBean(name = "defaultRestTemplate")
  public RestTemplate defaultRestTemplate(HttpClient httpClient) {
    RestTemplate restTemplate = new RestTemplate(
        new HttpComponentsClientHttpRequestFactory(httpClient));
    restTemplate.getMessageConverters().stream()
        .filter(c -> c instanceof MappingJackson2HttpMessageConverter)
        .forEach(c -> ((MappingJackson2HttpMessageConverter) c)
            .setObjectMapper(JSONUtils.transferObjectMapper()));
    return restTemplate;
  }

}
