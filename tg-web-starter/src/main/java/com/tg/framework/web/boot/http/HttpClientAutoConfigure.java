package com.tg.framework.web.boot.http;

import com.tg.framework.web.http.GeneralHttpClientBuilder;
import com.tg.framework.web.http.HttpClientProperties;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnClass(HttpClient.class)
public class HttpClientAutoConfigure {

  @Bean("httpClientProperties")
  @ConfigurationProperties(prefix = "tg.web.http-client")
  public HttpClientProperties httpClientProperties() {
    return GeneralHttpClientBuilder.generalHttpClientProperties();
  }

  @Bean("cookieStore")
  @ConditionalOnMissingBean
  public CookieStore cookieStore() {
    return new BasicCookieStore();
  }

  @Primary
  @Bean("httpClient")
  public HttpClient httpClient(
      @Qualifier("httpClientProperties") HttpClientProperties httpClientProperties,
      CookieStore cookieStore) {
    return GeneralHttpClientBuilder.generalHttpClient(httpClientProperties, cookieStore);
  }


}
