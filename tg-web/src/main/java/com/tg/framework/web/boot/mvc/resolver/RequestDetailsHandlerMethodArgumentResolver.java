package com.tg.framework.web.boot.mvc.resolver;

import com.tg.framework.commons.http.RequestDetails;
import com.tg.framework.web.util.ServletUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestDetailsHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return RequestDetails.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    return ServletUtils.getRequestDetails(webRequest.getNativeRequest(HttpServletRequest.class));
  }
}
