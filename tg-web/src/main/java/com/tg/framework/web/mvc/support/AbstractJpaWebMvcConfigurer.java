package com.tg.framework.web.mvc.support;

import com.tg.framework.web.ip.RequestDetailsResolver;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class AbstractJpaWebMvcConfigurer implements WebMvcConfigurer {

  @RestControllerAdvice
  static class SecurityControllerAdvice extends ControllerAdviceSupport {

    public SecurityControllerAdvice(RequestDetailsResolver requestDetailsResolver) {
      super(requestDetailsResolver);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<DefaultError> handleException(EntityNotFoundException ex,
        HttpServletRequest request) {
      printError(ex, request);
      return notFound();
    }

  }

}
