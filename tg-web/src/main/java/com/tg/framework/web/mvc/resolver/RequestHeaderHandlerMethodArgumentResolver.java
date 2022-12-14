package com.tg.framework.web.mvc.resolver;

import com.tg.framework.web.mvc.resolver.annotation.RequestHeader;
import com.tg.framework.web.util.HttpUtils;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestHeaderHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(RequestHeader.class) != null && String.class
        .isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    RequestHeader requestHeader = parameter.getParameterAnnotation(RequestHeader.class);
    Assert.state(requestHeader != null, "Annotation is missing");
    String value = HttpUtils
        .getHeader(webRequest.getNativeRequest(HttpServletRequest.class), requestHeader.value());
    if (requestHeader.required() && StringUtils.isBlank(value)) {
      throw new IllegalArgumentException("A '" + requestHeader.value() + "' header is required");
    }
    return value;
  }


}
