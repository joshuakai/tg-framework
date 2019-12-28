package com.tg.framework.web.mvc.resolver;

import com.tg.framework.commons.lang.StringOptional;
import com.tg.framework.web.mvc.resolver.annotation.RequestIp;
import com.tg.framework.web.ip.IpResolver;
import com.tg.framework.web.ip.support.IpResolverStrategy;
import com.tg.framework.web.util.HttpUtils;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestIpHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private IpResolver ipResolver;
  private Set<String> trustAgents;
  private boolean trustAgentsAcceptWildcard;

  public RequestIpHandlerMethodArgumentResolver(IpResolver ipResolver, Set<String> trustAgents,
      boolean trustAgentsAcceptWildcard) {
    Assert.notNull(ipResolver, "IpResolver must not be null.");
    this.ipResolver = ipResolver;
    this.trustAgents = trustAgents;
    this.trustAgentsAcceptWildcard = trustAgentsAcceptWildcard;
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
    String remoteAddress = request.getRemoteAddr();
    if (!HttpUtils.isLocalhost(remoteAddress) && !HttpUtils
        .isIpInWhitelist(remoteAddress, trustAgents, trustAgentsAcceptWildcard)) {
      return remoteAddress;
    }
    RequestIp requestIp = parameter.getParameterAnnotation(RequestIp.class);
    IpResolverStrategy strategy = requestIp.strategy();
    String headerName = requestIp.customHeaderName();
    Assert
        .isTrue(strategy != IpResolverStrategy.CUSTOM_HEADER || StringUtils.isNotBlank(headerName),
            "customHeaderName must not be empty when using IpResolverStrategy.CUSTOM_HEADER.");
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
        return StringOptional.ofNullable(HttpUtils.getHeader(request, headerName))
            .map(HttpUtils::convertLocalhost)
            .orElseGet(() -> HttpUtils.getRemoteAddr(request));
      default:
        return HttpUtils.getRemoteAddr(request);
    }
  }
}
