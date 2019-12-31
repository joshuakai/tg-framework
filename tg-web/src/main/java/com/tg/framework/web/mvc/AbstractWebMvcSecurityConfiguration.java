package com.tg.framework.web.mvc;

import com.tg.framework.web.mvc.resolver.PrincipalHandlerMethodArgumentResolver;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

public class AbstractWebMvcSecurityConfiguration extends AbstractWebMvcConfiguration {

  @RestControllerAdvice
  static class SecurityExceptionHandler {

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

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    super.addArgumentResolvers(argumentResolvers);
    argumentResolvers.add(new PrincipalHandlerMethodArgumentResolver());
  }
}
