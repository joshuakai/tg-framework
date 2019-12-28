package com.tg.framework.web.mvc;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

public class AbstractWebMvcSecurityConfiguration extends AbstractWebMvcConfiguration {

  @RestControllerAdvice
  static class SecurityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleException(AuthenticationException ex) {
      return new ErrorDTO(HttpStatus.UNAUTHORIZED.getReasonPhrase(), ex.getMessage(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleException(AccessDeniedException ex) {
      return new ErrorDTO(HttpStatus.FORBIDDEN.getReasonPhrase(), ex.getMessage(), null);
    }

  }

}
