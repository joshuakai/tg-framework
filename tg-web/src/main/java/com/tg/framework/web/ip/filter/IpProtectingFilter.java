package com.tg.framework.web.ip.filter;

import com.tg.framework.web.ip.IpAccessVoter;
import com.tg.framework.web.ip.IpForbiddenHandler;
import com.tg.framework.web.ip.RequestDetailsResolver;
import com.tg.framework.web.util.HttpUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

public class IpProtectingFilter extends OncePerRequestFilter {

  private static Logger logger = LoggerFactory.getLogger(IpProtectingFilter.class);

  private RequestDetailsResolver requestDetailsResolver;
  private IpAccessVoter ipAccessVoter;
  private IpForbiddenHandler ipForbiddenHandler = IpForbiddenHandler.DEFAULT;

  public IpProtectingFilter(RequestDetailsResolver requestDetailsResolver,
      IpAccessVoter ipAccessVoter) {
    Assert.notNull(requestDetailsResolver, "A request details resolver must be set");
    Assert.notNull(ipAccessVoter, "A ip access voter must be set");
    this.requestDetailsResolver = requestDetailsResolver;
    this.ipAccessVoter = ipAccessVoter;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String ip = requestDetailsResolver.resolveRemoteAddr(request);
    if (!ipAccessVoter.vote(ip, request)) {
      logger.warn("Denied {} {} [{}] {x-forwarded-for: {}}", HttpUtils.getMethod(request),
          requestDetailsResolver.resolveUrl(request), ip, HttpUtils.getXForwardedFor(request));
      if (ipForbiddenHandler.onIpForbidden(request, response, ip)) {
        filterChain.doFilter(request, response);
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }

  public void setIpForbiddenHandler(IpForbiddenHandler ipForbiddenHandler) {
    Assert.notNull(ipForbiddenHandler, "A ip forbidden handler must be set");
    this.ipForbiddenHandler = ipForbiddenHandler;
  }

}
