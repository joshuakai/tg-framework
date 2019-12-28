package com.tg.framework.web.mvc;

import com.tg.framework.commons.exception.AuthenticationRequiredException;
import com.tg.framework.commons.exception.AuthorityRequiredException;
import com.tg.framework.commons.exception.BusinessException;
import com.tg.framework.commons.exception.HttpClientException;
import com.tg.framework.commons.exception.NestedException;
import com.tg.framework.commons.exception.NestedRuntimeException;
import com.tg.framework.commons.exception.ParameterException;
import com.tg.framework.commons.exception.ResourceNotFoundException;
import com.tg.framework.commons.exception.TransactionalException;
import com.tg.framework.commons.util.JSONUtils;
import com.tg.framework.web.ip.IpResolver;
import com.tg.framework.web.ip.support.DefaultIpResolver;
import com.tg.framework.web.ip.support.IpResolverStrategy;
import com.tg.framework.web.mvc.formatter.CompositeDateFormatter;
import com.tg.framework.web.mvc.formatter.LocalDateFormatter;
import com.tg.framework.web.mvc.formatter.LocalDateTimeFormatter;
import com.tg.framework.web.mvc.formatter.LocalTimeFormatter;
import com.tg.framework.web.mvc.resolver.PrincipalHandlerMethodArgumentResolver;
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
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class AbstractWebMvcConfiguration implements WebMvcConfigurer {

  private IpResolverStrategy ipResolverStrategy = IpResolverStrategy.AUTO;
  private String ipResolverCustomHeaderName;
  private Set<String> trustAgents;
  private boolean trustAgentsAcceptWildcard;

  @Bean
  public IpResolver ipResolver() {
    return new DefaultIpResolver(ipResolverStrategy, ipResolverCustomHeaderName, trustAgents,
        trustAgentsAcceptWildcard);
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
    argumentResolvers.add(new RequestIpHandlerMethodArgumentResolver(ipResolver(), trustAgents,
        trustAgentsAcceptWildcard));
    argumentResolvers.add(new RequestHeaderHandlerMethodArgumentResolver());
    argumentResolvers.add(new UserAgentHandlerMethodArgumentResolver());
    argumentResolvers.add(new RequestClientHandlerMethodArgumentResolver(ipResolver()));
    argumentResolvers.add(new PrincipalHandlerMethodArgumentResolver());
  }

  public IpResolverStrategy getIpResolverStrategy() {
    return ipResolverStrategy;
  }

  public void setIpResolverStrategy(IpResolverStrategy ipResolverStrategy) {
    this.ipResolverStrategy = ipResolverStrategy;
  }

  public String getIpResolverCustomHeaderName() {
    return ipResolverCustomHeaderName;
  }

  public void setIpResolverCustomHeaderName(String ipResolverCustomHeaderName) {
    this.ipResolverCustomHeaderName = ipResolverCustomHeaderName;
  }

  public Set<String> getTrustAgents() {
    return trustAgents;
  }

  public void setTrustAgents(Set<String> trustAgents) {
    this.trustAgents = trustAgents;
  }

  public boolean isTrustAgentsAcceptWildcard() {
    return trustAgentsAcceptWildcard;
  }

  public void setTrustAgentsAcceptWildcard(boolean trustAgentsAcceptWildcard) {
    this.trustAgentsAcceptWildcard = trustAgentsAcceptWildcard;
  }

  @RestControllerAdvice
  static class DefaultExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Throwable ex) {
      LOGGER.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
      return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage(),
          null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(MethodArgumentNotValidException ex) {
      String[] errors = ex.getBindingResult().getAllErrors().stream().map(objectError -> {
        StringBuilder sb = new StringBuilder(objectError.getObjectName());
        if (objectError instanceof FieldError) {
          FieldError fieldError = (FieldError) objectError;
          sb.append(".").append(fieldError.getField())
              .append(":").append(fieldError.getDefaultMessage());
        } else {
          sb.append(":").append(objectError.getDefaultMessage());
        }
        return sb.toString();
      }).toArray(String[]::new);
      return new ErrorDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(), null, errors);
    }

    @ExceptionHandler(NestedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(NestedException ex) {
      LOGGER.error("{} {}", ex.getCode(), ex.getArgs(), ex);
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(NestedRuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(NestedRuntimeException ex) {
      LOGGER.error("{} {}", ex.getCode(), ex.getArgs(), ex);
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(TransactionalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(TransactionalException ex) {
      LOGGER.error("{} {}", ex.getCode(), ex.getArgs(), ex);
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(AuthenticationRequiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleException(AuthenticationRequiredException ex) {
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(AuthorityRequiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleException(AuthorityRequiredException ex) {
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(HttpClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(HttpClientException ex) {
      LOGGER.error("{} {}", ex.getCode(), ex.getArgs(), ex);
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(ParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ParameterException ex) {
      LOGGER.error("{} {}", ex.getCode(), ex.getArgs(), ex);
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleException(ResourceNotFoundException ex) {
      LOGGER.error("{} {}", ex.getCode(), ex.getArgs(), ex);
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(BusinessException.class)
    public ErrorDTO handleException(HttpServletResponse response, BusinessException ex) {
      response.setStatus(456);
      LOGGER.error("{} {}", ex.getCode(), ex.getArgs(), ex);
      return new ErrorDTO(ex.getCode(), ex.getMessage(), ex.getArgs());
    }

  }

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

}
