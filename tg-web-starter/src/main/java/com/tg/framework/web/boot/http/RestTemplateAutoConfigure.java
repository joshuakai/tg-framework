package com.tg.framework.web.boot.http;

import com.tg.framework.commons.util.JSONUtils;
import java.nio.charset.StandardCharsets;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import(HttpClientAutoConfigure.class)
@ConditionalOnProperty(prefix = "tg.web.rest-template", value = "enabled")
public class RestTemplateAutoConfigure {

  @Primary
  @Bean("restTemplate")
  public RestTemplate restTemplate(HttpClient httpClient) {
    RestTemplate restTemplate = new RestTemplate(
        new HttpComponentsClientHttpRequestFactory(httpClient));
    restTemplate.getMessageConverters().forEach(c -> {
      if (c instanceof StringHttpMessageConverter) {
        ((StringHttpMessageConverter) c).setDefaultCharset(StandardCharsets.UTF_8);
      } else if (c instanceof MappingJackson2HttpMessageConverter) {
        ((MappingJackson2HttpMessageConverter) c)
            .setObjectMapper(JSONUtils.transferObjectMapper());
      }
    });
    return restTemplate;
  }

}
