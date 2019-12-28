package com.tg.framework.cloud.boot;

import com.tg.framework.web.boot.EnableDefaultWebApp;
import com.tg.framework.web.util.RestTemplateUtils;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableDefaultWebApp
public class DefaultCloudAppConfig {

  @Primary
  @Bean("cloudRestTemplate")
  @LoadBalanced
  public RestTemplate cloudRestTemplate(MappingJackson2HttpMessageConverter messageConverter) {
    return RestTemplateUtils.buildDefaultRestTemplate(messageConverter);
  }

}
