package com.tg.framework.web.boot.mvc.resolver;

import com.tg.framework.commons.http.RequestClient;
import com.tg.framework.web.http.IpResolver;
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

  private IpResolver ipResolver;

  public RequestClientHandlerMethodArgumentResolver(IpResolver ipResolver) {
    Assert.notNull(ipResolver, "IpResolver must not be null.");
    this.ipResolver = ipResolver;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return RequestClient.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    String url = HttpUtils.getUrl(request);
    String requestIp = ipResolver.resolve(request);
    String remoteAddress = HttpUtils.getRemoteAddr(request);
    String xForwardedFor = HttpUtils.getRawXForwardedFor(request);
    String xRealIp = HttpUtils.getXRealIp(request);
    UserAgent userAgent = HttpUtils.getUserAgent(request);
    return new RequestClient(url, requestIp, remoteAddress, xForwardedFor, xRealIp, userAgent);
  }
}
