package com.tg.framework.web.mvc.handler;

import com.tg.framework.commons.concurrent.lock.exception.LockException;
import com.tg.framework.commons.concurrent.task.exception.TaskMutexException;
import com.tg.framework.commons.exception.BusinessException;
import com.tg.framework.commons.exception.NestedCodedException;
import com.tg.framework.commons.exception.NestedCodedRuntimeException;
import com.tg.framework.commons.exception.NestedException;
import com.tg.framework.commons.exception.NestedRuntimeException;
import com.tg.framework.commons.exception.ParamException;
import com.tg.framework.commons.exception.ParamInvalidException;
import com.tg.framework.commons.exception.ParamRequiredException;
import com.tg.framework.commons.exception.ResourceNotFoundException;
import com.tg.framework.commons.http.exception.RequestHeaderRequiredException;
import com.tg.framework.web.ip.RequestDetailsResolver;
import com.tg.framework.web.upload.support.UploadException;
import com.tg.framework.web.util.HttpUtils;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingMatrixVariableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@RestControllerAdvice
public class DefaultExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

  private static final String REQUEST_LOGGER_TEMPLATE = "%s %s [%s] {x-forwarded-for: %s}";
  private static final String TAB = "\t";
  private static final String DOT = ".";
  private static final String COLON = ":";

  private RequestDetailsResolver requestDetailsResolver;

  public DefaultExceptionHandler(
      RequestDetailsResolver requestDetailsResolver) {
    this.requestDetailsResolver = requestDetailsResolver;
  }

  protected String getLoggerTemplate(HttpServletRequest request) {
    return String.format(REQUEST_LOGGER_TEMPLATE, HttpUtils.getMethod(request),
        requestDetailsResolver.resolveUrl(request),
        requestDetailsResolver.resolveRemoteAddr(request), HttpUtils.getXForwardedFor(request));
  }

  protected String getLoggerTemplate(HttpServletRequest request, String more) {
    return getLoggerTemplate(request) + System.lineSeparator() + TAB + more;
  }

  @ExceptionHandler(Throwable.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorDTO handleException(Throwable ex, HttpServletRequest request) {
    LOGGER.error(getLoggerTemplate(request), ex);
    return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
  }

  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class,
      UnsupportedEncodingException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleException(Exception ex, HttpServletRequest request) {
    LOGGER.error(getLoggerTemplate(request), ex);
    return new ErrorDTO(ParamInvalidException.PRESENT_CODE, ex.getMessage());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleException(MissingServletRequestParameterException ex) {
    return new ErrorDTO(ParamRequiredException.PRESENT_CODE, ex.getParameterName());
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleException(MissingRequestHeaderException ex) {
    return new ErrorDTO(RequestHeaderRequiredException.PRESENT_CODE, ex.getHeaderName());
  }

  @ExceptionHandler(MissingRequestCookieException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleException(MissingRequestCookieException ex) {
    return new ErrorDTO("Http#CookieRequired", ex.getCookieName());
  }

  @ExceptionHandler(MissingPathVariableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleException(MissingPathVariableException ex) {
    return new ErrorDTO("Http#PathVariableRequired", ex.getVariableName());
  }

  @ExceptionHandler(MissingMatrixVariableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleException(MissingMatrixVariableException ex) {
    return new ErrorDTO("Http#MatrixVariableRequired", ex.getVariableName());
  }

  @ExceptionHandler(ServletRequestBindingException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleException(ServletRequestBindingException ex) {
    return new ErrorDTO(null, ex.getMessage());
  }

  private static String[] getErrors(BindingResult bindingResult) {
    return bindingResult.getAllErrors().stream().map(objectError -> {
      StringBuilder sb = new StringBuilder(objectError.getObjectName());
      if (objectError instanceof FieldError) {
        FieldError fieldError = (FieldError) objectError;
        sb.append(DOT).append(fieldError.getField())
            .append(COLON).append(fieldError.getDefaultMessage());
      } else {
        sb.append(COLON).append(objectError.getDefaultMessage());
      }
      return sb.toString();
    }).toArray(String[]::new);
  }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleException(BindException ex) {
    return new ErrorDTO(ParamInvalidException.PRESENT_CODE, getErrors(ex.getBindingResult()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleException(MethodArgumentNotValidException ex) {
    return new ErrorDTO(ParamInvalidException.PRESENT_CODE, getErrors(ex.getBindingResult()));
  }

  @ExceptionHandler(RestClientException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleException(RestClientException ex, HttpServletRequest request) {
    LOGGER.error(getLoggerTemplate(request), ex);
    return new ErrorDTO(HttpStatus.BAD_REQUEST.name(), ex.getMessage());
  }

  @ExceptionHandler(RestClientResponseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleException(RestClientResponseException ex, HttpServletRequest request) {
    LOGGER.error(getLoggerTemplate(request), ex);
    return new ErrorDTO(ex.getStatusText(), ex.getResponseBodyAsString());
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
