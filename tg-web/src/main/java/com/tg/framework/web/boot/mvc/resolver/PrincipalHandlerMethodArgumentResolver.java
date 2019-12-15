package com.tg.framework.web.boot.mvc.resolver;

import com.tg.framework.core.exception.AuthenticationRequiredException;
import com.tg.framework.core.security.SecurityUtils;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
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
    boolean isString = String.class.isAssignableFrom(parameter.getParameterType());
    Optional optional =
        isString ? SecurityUtils.getPrincipalAsString() : SecurityUtils.getPrincipal();
    if (optional.isPresent()) {
      return optional.get();
    }
    Principal principal = parameter.getParameterAnnotation(Principal.class);
    if (principal.required()) {
      throw new AuthenticationRequiredException();
    }
    return isString ? StringUtils.EMPTY : null;
  }
}
