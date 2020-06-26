package com.tg.framework.web.mvc.support;

import com.tg.framework.commons.BusinessException;
import com.tg.framework.commons.FrameworkMessageSource;
import com.tg.framework.web.ip.RequestDetailsResolver;
import com.tg.framework.web.util.HttpUtils;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestClientResponseException;

public abstract class ControllerAdviceSupport implements MessageSourceAware {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected MessageSourceAccessor messages = FrameworkMessageSource.getAccessor();
  protected RequestDetailsResolver requestDetailsResolver;

  public ControllerAdviceSupport(RequestDetailsResolver requestDetailsResolver) {
    this.requestDetailsResolver = requestDetailsResolver;
  }

  @Override
  public void setMessageSource(MessageSource messageSource) {
    Assert.notNull(messageSource, "A message source must be set");
    messages = new MessageSourceAccessor(messageSource);
  }

  protected void printError(Throwable throwable, HttpServletRequest request) {
    logger.error(
        "{} {} [{}] {x-forwarded-for: {}}",
        request.getMethod(),
        requestDetailsResolver.resolveUrl(request),
        requestDetailsResolver.resolveRemoteAddr(request),
        HttpUtils.getXForwardedFor(request),
        throwable
    );
  }

  protected void printError(BusinessException ex, HttpServletRequest request) {
    logger.error(
        "{} {} [{}] {x-forwarded-for: {}} {code: {}, args: {}}",
        request.getMethod(),
        requestDetailsResolver.resolveUrl(request),
        requestDetailsResolver.resolveRemoteAddr(request),
        HttpUtils.getXForwardedFor(request),
        ex.getCode(),
        ex.getArgs(),
        ex
    );
  }

  protected void printError(RestClientResponseException ex, HttpServletRequest request) {
    logger.error(
        "{} {} [{}] {x-forwarded-for: {}} {status: {}, body: {}}",
        request.getMethod(),
        requestDetailsResolver.resolveUrl(request),
        requestDetailsResolver.resolveRemoteAddr(request),
        HttpUtils.getXForwardedFor(request),
        ex.getStatusText(),
        ex.getResponseBodyAsString(),
        ex
    );
  }

  protected String getDefaultBadRequestMessage() {
    return messages.getMessage("Web.Error.BadRequest", "Bad Request");
  }

  protected String getDefaultUnauthorizedMessage() {
    return messages.getMessage("Web.Error.Unauthorized", "Unauthorized");
  }

  protected String getDefaultForbiddenMessage() {
    return messages.getMessage("Web.Error.Forbidden", "Forbidden");
  }

  protected String getDefaultNotFoundMessage() {
    return messages.getMessage("Web.Error.NotFound", "Not found");
  }

  protected String getDefaultInternalServerErrorMessage() {
    return messages.getMessage("Web.Error.InternalServerError", "Internal Server Error");
  }

  protected ResponseEntity<DefaultError> badRequest() {
    return badRequest(getDefaultBadRequestMessage());
  }

  protected ResponseEntity<DefaultError> unauthorized() {
    return unauthorized(getDefaultUnauthorizedMessage());
  }

  protected ResponseEntity<DefaultError> forbidden() {
    return forbidden(getDefaultForbiddenMessage());
  }

  protected ResponseEntity<DefaultError> notFound() {
    return notFound(getDefaultNotFoundMessage());
  }

  protected ResponseEntity<DefaultError> internalServerError() {
    return internalServerError(getDefaultInternalServerErrorMessage());
  }

  protected static ResponseEntity<DefaultError> badRequest(String message) {
    return defaultError(HttpStatus.BAD_REQUEST, message);
  }

  protected static ResponseEntity<DefaultError> unauthorized(String message) {
    return defaultError(HttpStatus.UNAUTHORIZED, message);
  }

  protected static ResponseEntity<DefaultError> forbidden(String message) {
    return defaultError(HttpStatus.FORBIDDEN, message);
  }

  protected static ResponseEntity<DefaultError> notFound(String message) {
    return defaultError(HttpStatus.NOT_FOUND, message);
  }

  protected static ResponseEntity<DefaultError> internalServerError(String message) {
    return defaultError(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }

  protected static ResponseEntity<DefaultError> defaultError(HttpStatus status, String message) {
    return defaultError(status, String.valueOf(HttpStatus.BAD_REQUEST.value()), message);
  }

  protected static ResponseEntity<DefaultError> defaultError(HttpStatus status, String type,
      String message) {
    return new ResponseEntity<>(new DefaultError(type, message), status);
  }

  protected static ResponseEntity<BindingResultError> bindingResultError(
      BindingResult bindingResult) {
    Map<String, String> errors = new LinkedHashMap<>();
    if (bindingResult.hasErrors()) {
      bindingResult.getAllErrors().forEach(objectError -> {
        if (objectError instanceof FieldError) {
          FieldError fieldError = (FieldError) objectError;
          errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        } else {
          errors.put(objectError.getObjectName(), objectError.getDefaultMessage());
        }
      });
    }
    return new ResponseEntity<>(new BindingResultError(errors), HttpStatus.BAD_REQUEST);
  }

  protected static ResponseEntity<BusinessError> businessError(HttpStatus status, String code,
      String message) {
    return new ResponseEntity<>(new BusinessError(code, message), status);
  }


  public static final String BUSINESS = "Business";
  public static final String VALIDATION = "Validation";


  public static abstract class AbstractTypedError implements Serializable {

    private static final long serialVersionUID = 4912854681636896960L;

    private final String type;

    public AbstractTypedError(String type) {
      this.type = type;
    }

    public String getType() {
      return type;
    }

  }

  public static class DefaultError extends AbstractTypedError {

    private static final long serialVersionUID = 7515005544778831788L;

    private final String message;

    public DefaultError(String type, String message) {
      super(type);
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }

  public static class BindingResultError extends AbstractTypedError {

    private static final long serialVersionUID = -1739356712578002140L;

    private final Map<String, String> errors;

    public BindingResultError(Map<String, String> errors) {
      super(VALIDATION);
      this.errors = errors;
    }

    public Map<String, String> getErrors() {
      return errors;
    }

  }

  public static class BusinessError extends DefaultError {

    private static final long serialVersionUID = -8158690326526632705L;

    private final String code;

    public BusinessError(String code, String message) {
      super(BUSINESS, message);
      this.code = code;
    }

    public String getCode() {
      return code;
    }

  }

}
