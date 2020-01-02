package com.tg.framework.web.mvc.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityExceptionHandler extends DefaultExceptionHandler {

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorDTO handleException(AuthenticationException ex) {
    return new ErrorDTO(null, ex.getMessage());
  }

  @ExceptionHandler(java.nio.file.AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorDTO handleException(java.nio.file.AccessDeniedException ex) {
    return new ErrorDTO(null, ex.getMessage());
  }

}
