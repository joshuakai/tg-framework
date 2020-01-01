package com.tg.framework.web.mvc.resolver;

import com.tg.framework.commons.http.RequestClient;
import com.tg.framework.web.ip.RequestDetailsResolver;
import com.tg.framework.web.util.HttpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestClientHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private RequestDetailsResolver requestDetailsResolver;

  public RequestClientHandlerMethodArgumentResolver(RequestDetailsResolver requestDetailsResolver) {
    Assert.notNull(requestDetailsResolver, "RequestDetailsResolver must not be null.");
    this.requestDetailsResolver = requestDetailsResolver;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return RequestClient.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    String url = requestDetailsResolver.resolveUrl(request);
    String requestIp = requestDetailsResolver.resolveRemoteAddr(request);
    String remoteAddress = HttpUtils.getRemoteAddr(request);
    String xForwardedFor = HttpUtils.getXForwardedFor(request);
    String xRealIp = HttpUtils.getXRealIp(request);
    UserAgent userAgent = HttpUtils.getUserAgent(request);
    return new RequestClient(url, requestIp, remoteAddress, xForwardedFor, xRealIp, userAgent);
  }
}
