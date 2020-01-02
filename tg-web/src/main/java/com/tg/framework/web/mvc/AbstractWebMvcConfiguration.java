package com.tg.framework.web.mvc;

import com.tg.framework.commons.util.JSONUtils;
import com.tg.framework.web.ip.RequestDetailsResolver;
import com.tg.framework.web.ip.support.XForwardedRequestDetailsResolver;
import com.tg.framework.web.mvc.formatter.CompositeDateFormatter;
import com.tg.framework.web.mvc.formatter.LocalDateFormatter;
import com.tg.framework.web.mvc.formatter.LocalDateTimeFormatter;
import com.tg.framework.web.mvc.formatter.LocalTimeFormatter;
import com.tg.framework.web.mvc.handler.DefaultExceptionHandler;
import com.tg.framework.web.mvc.resolver.RequestClientHandlerMethodArgumentResolver;
import com.tg.framework.web.mvc.resolver.RequestHeaderHandlerMethodArgumentResolver;
import com.tg.framework.web.mvc.resolver.RequestIpHandlerMethodArgumentResolver;
import com.tg.framework.web.mvc.resolver.UserAgentHandlerMethodArgumentResolver;
import com.tg.framework.web.util.RestTemplateUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class AbstractWebMvcConfiguration implements WebMvcConfigurer {

  @Bean
  @ConfigurationProperties("tg.web.x-forwarded")
  public RequestDetailsResolver requestDetailsResolver() {
    return new XForwardedRequestDetailsResolver();
  }

  @Bean
  public DefaultExceptionHandler defaultExceptionHandler() {
    return new DefaultExceptionHandler();
  }

  @Bean
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
    return new MappingJackson2HttpMessageConverter(JSONUtils.transferObjectMapper());
  }

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setUseCodeAsDefaultMessage(true);
    return messageSource;
  }

  @Bean
  public Validator hibernateValidator() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.setProviderClass(HibernateValidator.class);
    validator.setValidationMessageSource(messageSource());
    return validator;
  }

  @Bean("defaultRestTemplate")
  public RestTemplate defaultRestTemplate() {
    return RestTemplateUtils.buildDefaultRestTemplate(mappingJackson2HttpMessageConverter());
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(mappingJackson2HttpMessageConverter());
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addFormatterForFieldType(Date.class, new CompositeDateFormatter());
    registry.addFormatterForFieldType(LocalDateTime.class, new LocalDateTimeFormatter());
    registry.addFormatterForFieldType(LocalDate.class, new LocalDateFormatter());
    registry.addFormatterForFieldType(LocalTime.class, new LocalTimeFormatter());
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    SortHandlerMethodArgumentResolver sortArgumentResolver = new SortHandlerMethodArgumentResolver();
    sortArgumentResolver.setSortParameter("_sort");
    PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver(
        sortArgumentResolver);
    pageableArgumentResolver.setPrefix("_");
    argumentResolvers.add(pageableArgumentResolver);
    argumentResolvers.add(new RequestIpHandlerMethodArgumentResolver(requestDetailsResolver()));
    argumentResolvers.add(new RequestHeaderHandlerMethodArgumentResolver());
    argumentResolvers.add(new UserAgentHandlerMethodArgumentResolver());
    argumentResolvers.add(new RequestClientHandlerMethodArgumentResolver(requestDetailsResolver()));
  }

}
