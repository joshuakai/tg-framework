package com.tg.framework.web.ip.interceptor;

import com.tg.framework.web.ip.IpAccessVoter;
import com.tg.framework.web.ip.IpForbiddenHandler;
import com.tg.framework.web.ip.RequestDetailsResolver;
import com.tg.framework.web.util.HttpUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;

public class IpProtectingInterceptor implements HandlerInterceptor {

  private static Logger logger = LoggerFactory.getLogger(IpProtectingInterceptor.class);

  private RequestDetailsResolver requestDetailsResolver;
  private IpAccessVoter ipAccessVoter;
  private IpForbiddenHandler ipForbiddenHandler = IpForbiddenHandler.DEFAULT;

  public IpProtectingInterceptor(RequestDetailsResolver requestDetailsResolver,
      IpAccessVoter ipAccessVoter) {
    Assert.notNull(requestDetailsResolver, "A request details resolver must be set");
    Assert.notNull(ipAccessVoter, "A ip access voter must be set");
    this.requestDetailsResolver = requestDetailsResolver;
    this.ipAccessVoter = ipAccessVoter;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String ip = requestDetailsResolver.resolveRemoteAddr(request);
    if (!ipAccessVoter.vote(ip, request)) {
      logger.warn("Denied {} {} [{}] {x-forwarded-for: {}}", HttpUtils.getMethod(request),
          requestDetailsResolver.resolveUrl(request), ip, HttpUtils.getXForwardedFor(request));
      return ipForbiddenHandler.onIpForbidden(request, response, ip);
    }
    return true;
  }

  public void setIpForbiddenHandler(IpForbiddenHandler ipForbiddenHandler) {
    Assert.notNull(ipForbiddenHandler, "A ip forbidden handler must be set");
    this.ipForbiddenHandler = ipForbiddenHandler;
  }

}
