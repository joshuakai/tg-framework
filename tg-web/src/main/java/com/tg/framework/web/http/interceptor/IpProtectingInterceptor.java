package com.tg.framework.web.http.interceptor;

import com.tg.framework.web.http.IpAccessVoter;
import com.tg.framework.web.http.IpForbiddenHandler;
import com.tg.framework.web.http.IpResolver;
import com.tg.framework.web.http.support.DefaultIpForbiddenHandler;
import com.tg.framework.web.util.HttpUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;

public class IpProtectingInterceptor implements HandlerInterceptor {

  private static Logger logger = LoggerFactory.getLogger(IpProtectingInterceptor.class);

  private IpResolver ipResolver;
  private IpAccessVoter ipAccessVoter;
  private IpForbiddenHandler ipForbiddenHandler = new DefaultIpForbiddenHandler();

  public IpProtectingInterceptor(IpResolver ipResolver, IpAccessVoter ipAccessVoter) {
    Assert.notNull(ipResolver, "IpResolver must not be null.");
    Assert.notNull(ipAccessVoter, "IpAccessVoter must not be null.");
    this.ipResolver = ipResolver;
    this.ipAccessVoter = ipAccessVoter;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String ip = ipResolver.resolve(request);
    if (!ipAccessVoter.vote(ip, request)) {
      logger.warn("IP access denied: {} {}.", ip, HttpUtils.getUrl(request));
      return ipForbiddenHandler.onIpForbidden(request, response, ip);
    }
    return true;
  }

  public void setIpForbiddenHandler(IpForbiddenHandler ipForbiddenHandler) {
    Assert.notNull(ipForbiddenHandler, "IpForbiddenHandler must not be null.");
    this.ipForbiddenHandler = ipForbiddenHandler;
  }

}
