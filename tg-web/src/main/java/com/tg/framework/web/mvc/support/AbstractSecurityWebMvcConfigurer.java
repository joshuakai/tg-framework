package com.tg.framework.web.mvc.support;

import com.tg.framework.web.ip.RequestDetailsResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class AbstractSecurityWebMvcConfigurer implements WebMvcConfigurer {

  @RestControllerAdvice
  static class SecurityControllerAdvice extends ControllerAdviceSupport {

    public SecurityControllerAdvice(RequestDetailsResolver requestDetailsResolver) {
      super(requestDetailsResolver);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DefaultError> handleException(AuthenticationException ex) {
      return unauthorized(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<DefaultError> handleException(AccessDeniedException ex) {
      return forbidden(ex.getMessage());
    }

  }

}
