package com.tg.framework.web.mvc.resolver;

import com.tg.framework.web.ip.RequestDetailsResolver;
import com.tg.framework.web.mvc.resolver.annotation.RequestIp;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestIpHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private RequestDetailsResolver requestDetailsResolver;

  public RequestIpHandlerMethodArgumentResolver(RequestDetailsResolver requestDetailsResolver) {
    Assert.notNull(requestDetailsResolver, "A request details resolver must be set");
    this.requestDetailsResolver = requestDetailsResolver;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(RequestIp.class) != null && String.class
        .isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    return requestDetailsResolver.resolveRemoteAddr(request);
  }
}
