package com.tg.framework.web.mvc.support;

import com.tg.framework.commons.BusinessException;
import com.tg.framework.commons.DataNotFoundException;
import com.tg.framework.commons.concurrent.lock.LockException;
import com.tg.framework.commons.concurrent.task.TaskMutexException;
import com.tg.framework.commons.util.JSONUtils;
import com.tg.framework.web.ip.RequestDetailsResolver;
import com.tg.framework.web.mvc.formatter.CompositeDateFormatter;
import com.tg.framework.web.mvc.formatter.LocalDateFormatter;
import com.tg.framework.web.mvc.formatter.LocalDateTimeFormatter;
import com.tg.framework.web.mvc.formatter.LocalTimeFormatter;
import com.tg.framework.web.mvc.resolver.RequestClientHandlerMethodArgumentResolver;
import com.tg.framework.web.mvc.resolver.RequestHeaderHandlerMethodArgumentResolver;
import com.tg.framework.web.mvc.resolver.RequestIpHandlerMethodArgumentResolver;
import com.tg.framework.web.mvc.resolver.UserAgentHandlerMethodArgumentResolver;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class AbstractGeneralWebMvcConfigurer implements WebMvcConfigurer {

  @Resource
  private RequestDetailsResolver requestDetailsResolver;

  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames("classpath:messages", "classpath:com/tg/framework/messages");
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
    return messageSource;
  }

  @Bean
  public Validator hibernateValidator() {
    return Validation
        .byProvider(HibernateValidator.class)
        .configure()
        .failFast(true)
        .buildValidatorFactory()
        .getValidator();
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.forEach(c -> {
      if (c instanceof StringHttpMessageConverter) {
        ((StringHttpMessageConverter) c).setDefaultCharset(StandardCharsets.UTF_8);
      } else if (c instanceof MappingJackson2HttpMessageConverter) {
        ((MappingJackson2HttpMessageConverter) c).setObjectMapper(JSONUtils.transferObjectMapper());
      }
    });
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
    argumentResolvers.add(new RequestHeaderHandlerMethodArgumentResolver());
    argumentResolvers.add(new UserAgentHandlerMethodArgumentResolver());
    argumentResolvers.add(new RequestIpHandlerMethodArgumentResolver(requestDetailsResolver));
    argumentResolvers.add(new RequestClientHandlerMethodArgumentResolver(requestDetailsResolver));
  }

  @RestControllerAdvice
  static class GeneralControllerAdvice extends ControllerAdviceSupport {

    public GeneralControllerAdvice(RequestDetailsResolver requestDetailsResolver) {
      super(requestDetailsResolver);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessError> handleException(BusinessException ex,
        HttpServletRequest request) {
      printError(ex, request);
      HttpStatus status =
          ex instanceof DataNotFoundException ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
      String message =
          ex.isI18n() ? messages.getMessage(ex.getI18nCode(), ex.getI18nArgs(), ex.getMessage())
              : ex.getMessage();
      return businessError(status, ex.getCode(), message);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BindingResultError> handleException(BindException ex) {
      return bindingResultError(ex.getBindingResult());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BindingResultError> handleException(MethodArgumentNotValidException ex) {
      return bindingResultError(ex.getBindingResult());
    }

    @ExceptionHandler({LockException.class, TaskMutexException.class,
        ServletRequestBindingException.class, UnsupportedEncodingException.class})
    public ResponseEntity<DefaultError> handleException(Exception ex) {
      return badRequest(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DefaultError> handleException(Exception ex, HttpServletRequest request) {
      printError(ex, request);
      return badRequest();
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<DefaultError> handleException(Throwable ex, HttpServletRequest request) {
      printError(ex, request);
      return internalServerError();
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<DefaultError> handleException(RestClientException ex,
        HttpServletRequest request) {
      printError(ex, request);
      return badRequest();
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<DefaultError> handleException(RestClientResponseException ex,
        HttpServletRequest request) {
      printError(ex, request);
      return badRequest();
    }

  }

}
