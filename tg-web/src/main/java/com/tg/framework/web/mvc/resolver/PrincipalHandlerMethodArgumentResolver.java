package com.tg.framework.web.mvc.resolver;

import com.tg.framework.commons.util.SecurityUtils;
import com.tg.framework.web.mvc.resolver.annotation.Principal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PrincipalHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(Principal.class) != null;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    Class<?> clazz = parameter.getParameterType();
    boolean isString = String.class.isAssignableFrom(clazz);
    Object principal;
    if (isString) {
      principal = SecurityUtils.getPrincipalAsString().orElse(null);
    } else {
      principal = SecurityUtils.getPrincipal().orElse(null);
    }
    if (principal == null) {
      Principal annotation = parameter.getParameterAnnotation(Principal.class);
      if (annotation == null) {
        throw new InternalAuthenticationServiceException("Annotation is missing");
      }
      if (annotation.required()) {
        throw new AccessDeniedException("Access is denied");
      }
      return isString ? StringUtils.EMPTY : null;
    }
    if (!clazz.isAssignableFrom(principal.getClass())) {
      throw new InternalAuthenticationServiceException(
          "Principal does not match the parameter type");
    }
    return principal;
  }

}
