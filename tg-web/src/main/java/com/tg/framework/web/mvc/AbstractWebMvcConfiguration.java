package com.tg.framework.web.mvc;

import com.tg.framework.commons.concurrent.lock.exception.LockException;
import com.tg.framework.commons.concurrent.task.exception.TaskMutexException;
import com.tg.framework.commons.exception.BusinessException;
import com.tg.framework.commons.exception.NestedCodedException;
import com.tg.framework.commons.exception.NestedCodedRuntimeException;
import com.tg.framework.commons.exception.NestedException;
import com.tg.framework.commons.exception.NestedRuntimeException;
import com.tg.framework.commons.exception.ParamException;
import com.tg.framework.commons.exception.ParamInvalidException;
import com.tg.framework.commons.exception.ResourceNotFoundException;
import com.tg.framework.commons.http.exception.HttpClientException;
import com.tg.framework.commons.http.exception.RequestHeaderRequiredException;
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
import com.tg.framework.web.upload.support.UploadException;
import com.tg.framework.web.util.HttpUtils;
import com.tg.framework.web.util.RestTemplateUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ArrayUtils;
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

    @Resource
    private IpResolver ipResolver;

    private String getLoggerTemplate(HttpServletRequest request) {
      return String
          .format("%s %s [%s] [%s].", HttpUtils.getMethod(request), HttpUtils.getUrl(request),
              ipResolver.resolve(request), HttpUtils.getContentType(request));
    }

    private String getLoggerTemplate(HttpServletRequest request, String more) {
      return String
          .format("%s %s [%s] [%s] %s.", HttpUtils.getMethod(request), HttpUtils.getUrl(request),
              ipResolver.resolve(request), HttpUtils.getContentType(request), more);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Throwable ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request), ex);
      return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
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
      return new ErrorDTO(ParamInvalidException.PRESENT_CODE, errors);
    }

    @ExceptionHandler(NestedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(NestedException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request), ex);
      return new ErrorDTO(null, ex.getMessage());
    }

    @ExceptionHandler(NestedRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(NestedRuntimeException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request), ex);
      return new ErrorDTO(null, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(BusinessException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request, "{}"), ex.getArgs(), ex);
      return Optional.ofNullable(ex.getArgs())
          .map(args -> new ErrorDTO(null, ArrayUtils.toString(args), ex.getMessage()))
          .orElseGet(() -> new ErrorDTO(null, ex.getMessage()));
    }

    @ExceptionHandler(NestedCodedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(NestedCodedException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request, "{}"), ex.getCode(), ex);
      return new ErrorDTO(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(NestedCodedRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(NestedCodedRuntimeException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request, "{}"), ex.getCode(), ex);
      return new ErrorDTO(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(LockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(LockException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request, "{} {}"), ex.getCode(), ex.getKey(), ex);
      return new ErrorDTO(ex.getCode(), ex.getKey(), ex.getMessage());
    }

    @ExceptionHandler(TaskMutexException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(TaskMutexException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request, "{} {}"), ex.getCode(), ex.getKey(), ex);
      return new ErrorDTO(ex.getCode(), ex.getKey(), ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleException(ResourceNotFoundException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request, "{} {}"), ex.getCode(), ex.getResource(), ex);
      return new ErrorDTO(ex.getCode(), ex.getResource(), ex.getMessage());
    }

    @ExceptionHandler(HttpClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(HttpClientException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request, "{} {}"), ex.getCode(), ex.getStatusCode(), ex);
      return new ErrorDTO(ex.getCode(), String.valueOf(ex.getStatusCode()), ex.getMessage());
    }

    @ExceptionHandler(RequestHeaderRequiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(RequestHeaderRequiredException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request, "{} {}"), ex.getCode(), ex.getHeaderName(), ex);
      return new ErrorDTO(ex.getCode(), ex.getHeaderName(), ex.getMessage());
    }

    @ExceptionHandler(ParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ParamException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request, "{} {}"), ex.getCode(), ex.getParamName(), ex);
      return new ErrorDTO(ex.getCode(), ex.getParamName(), ex.getMessage());
    }

    @ExceptionHandler(ParamInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ParamInvalidException ex, HttpServletRequest request) {
      LOGGER.error(getLoggerTemplate(request, "{} {} {}"), ex.getCode(), ex.getParamName(),
          ex.getParamValue(), ex);
      Object paramValue = ex.getParamValue();
      String paramValueMessage;
      if (paramValue == null) {
        paramValueMessage = null;
      } else if (paramValue.getClass().isArray()) {
        paramValueMessage = ArrayUtils.toString(paramValue);
      } else {
        paramValueMessage = paramValue.toString();
      }
      return new ErrorDTO(ex.getCode(), ex.getParamName(), paramValueMessage, ex.getMessage());
    }

    @ExceptionHandler(UploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(UploadException ex) {
      return new ErrorDTO(null, ex.getOriginalFilename(), ex.getMessage());
    }

  }

  static class ErrorDTO {

    private String code;
    private String[] messages;

    public ErrorDTO(String code, String... messages) {
      this.code = code;
      this.messages = messages;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String[] getMessages() {
      return messages;
    }

    public void setMessages(String[] messages) {
      this.messages = messages;
    }
  }
}
