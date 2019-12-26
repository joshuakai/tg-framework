package com.tg.framework.web.boot.mvc.resolver;

import com.tg.framework.commons.lang.StringOptional;
import com.tg.framework.web.http.IpResolver;
import com.tg.framework.web.http.support.IpResolverStrategy;
import com.tg.framework.web.boot.mvc.resolver.annotation.RequestIp;
import com.tg.framework.web.util.HttpUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestIpHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private IpResolver ipResolver;

  public RequestIpHandlerMethodArgumentResolver(IpResolver ipResolver) {
    Assert.notNull(ipResolver, "IpResolver must not be null.");
    this.ipResolver = ipResolver;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(RequestIp.class) != null && String.class
        .isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    RequestIp requestIp = parameter.getParameterAnnotation(RequestIp.class);
    IpResolverStrategy strategy = requestIp.strategy();
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    switch (strategy) {
      case AUTO:
        return ipResolver.resolve(request);
      case REMOTE_ADDRESS:
        return HttpUtils.getRemoteAddr(request);
      case X_FORWARDED_FOR:
        return HttpUtils.getRemoteAddrPreferXForwardedFor(request);
      case X_REAL_IP:
        return HttpUtils.getRemoteAddrPreferXRealIp(request);
      case CUSTOM_HEADER:
        StringOptional.ofNullable(HttpUtils.getHeader(request, requestIp.customHeaderName()))
            .orElseGet(() -> HttpUtils.getRemoteAddr(request));
      default:
        return HttpUtils.getRemoteAddr(request);
    }
  }
}
