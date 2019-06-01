package com.tg.framework.web.boot.mvc;

import com.tg.framework.commons.util.JSONUtils;
import com.tg.framework.core.exception.BusinessException;
import com.tg.framework.core.exception.NestedException;
import com.tg.framework.core.exception.NestedRuntimeException;
import com.tg.framework.core.exception.TransactionalException;
import com.tg.framework.web.boot.mvc.formatter.CompositeDateFormatter;
import com.tg.framework.web.boot.mvc.formatter.LocalDateFormatter;
import com.tg.framework.web.boot.mvc.formatter.LocalDateTimeFormatter;
import com.tg.framework.web.boot.mvc.formatter.LocalTimeFormatter;
import com.tg.framework.web.boot.mvc.resolver.RequestDetailsHandlerMethodArgumentResolver;
import com.tg.framework.web.boot.mvc.resolver.UserAgentHandlerMethodArgumentResolver;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DefaultWebMvcConfig implements WebMvcConfigurer {

  static class ErrorDTO {

    private String error;
    private String message;
    private Object[] args;

    public ErrorDTO(String error, String message, Object[] args) {
      this.error = error;
      this.message = message;
      this.args = args;
    }

    public String getError() {
      return error;
    }

    public void setError(String error) {
      this.error = error;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public Object[] getArgs() {
      return args;
    }

    public void setArgs(Object[] args) {
      this.args = args;
    }
  }

  @ControllerAdvice
  static class DefaultExceptionHandler {

    @ExceptionHandler(NestedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleException(NestedException ex) {
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(NestedRuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleException(NestedRuntimeException ex) {
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(TransactionalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleException(TransactionalException ex) {
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ErrorDTO handleException(HttpServletResponse response, BusinessException ex) {
      response.setStatus(456);
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

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
    RestTemplate restTemplate = new RestTemplate();
    List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
    for (int i = 0; i < messageConverters.size(); i++) {
      if (MappingJackson2HttpMessageConverter.class == messageConverters.get(i).getClass()) {
        messageConverters.set(i, mappingJackson2HttpMessageConverter());
      }
    }
    return restTemplate;
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
    argumentResolvers.add(new UserAgentHandlerMethodArgumentResolver());
    argumentResolvers.add(new RequestDetailsHandlerMethodArgumentResolver());
  }

}
