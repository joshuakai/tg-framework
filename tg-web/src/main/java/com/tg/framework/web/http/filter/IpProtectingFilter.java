package com.tg.framework.web.http.filter;

import com.tg.framework.web.http.IpAccessVoter;
import com.tg.framework.web.http.IpForbiddenHandler;
import com.tg.framework.web.http.IpResolver;
import com.tg.framework.web.http.support.DefaultIpForbiddenHandler;
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

  private IpResolver ipResolver;
  private IpAccessVoter ipAccessVoter;
  private IpForbiddenHandler ipForbiddenHandler = new DefaultIpForbiddenHandler();

  public IpProtectingFilter(IpResolver ipResolver, IpAccessVoter ipAccessVoter) {
    Assert.notNull(ipResolver, "IpResolver must not be null.");
    Assert.notNull(ipAccessVoter, "IpAccessVoter must not be null.");
    this.ipResolver = ipResolver;
    this.ipAccessVoter = ipAccessVoter;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String ip = ipResolver.resolve(request);
    if (!ipAccessVoter.vote(ip, request)) {
      logger.warn("IP access denied: {} {}.", ip, HttpUtils.getUrl(request));
      if (ipForbiddenHandler.onIpForbidden(request, response, ip)) {
        filterChain.doFilter(request, response);
      }
    }
  }

  public void setIpForbiddenHandler(IpForbiddenHandler ipForbiddenHandler) {
    Assert.notNull(ipForbiddenHandler, "IpForbiddenHandler must not be null.");
    this.ipForbiddenHandler = ipForbiddenHandler;
  }

}
