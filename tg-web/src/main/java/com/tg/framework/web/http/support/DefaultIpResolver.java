package com.tg.framework.web.http.support;

import com.tg.framework.commons.lang.StringOptional;
import com.tg.framework.web.http.IpResolver;
import com.tg.framework.web.util.HttpUtils;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class DefaultIpResolver implements IpResolver {

  private IpResolverStrategy strategy;
  private String customHeaderName;
  private Set<String> trustAgents;
  private boolean trustAgentsAcceptWildcard;

  public DefaultIpResolver(IpResolverStrategy strategy) {
    this(strategy, null, null, false);
  }

  public DefaultIpResolver(IpResolverStrategy strategy, String customHeaderName,
      Set<String> trustAgents, boolean trustAgentsAcceptWildcard) {
    Assert.notNull(strategy, "IpResolverStrategy must not be null.");
    Assert.isTrue(
        strategy != IpResolverStrategy.CUSTOM_HEADER || StringUtils.isNotBlank(customHeaderName),
        "customHeaderName must not be empty when using IpResolverStrategy.CUSTOM_HEADER.");
    this.strategy = strategy;
    this.customHeaderName = customHeaderName;
    this.trustAgents = trustAgents;
    this.trustAgentsAcceptWildcard = trustAgentsAcceptWildcard;
  }

  @Override
  public String resolve(HttpServletRequest request) {
    String remoteAddress = HttpUtils.getRemoteAddr(request);
    if (!HttpUtils.isIpInWhitelist(remoteAddress, trustAgents, trustAgentsAcceptWildcard)) {
      return remoteAddress;
    }
    switch (strategy) {
      case REMOTE_ADDRESS:
        return HttpUtils.getRemoteAddr(request);
      case X_FORWARDED_FOR:
        return HttpUtils.getRemoteAddrPreferXForwardedFor(request);
      case X_REAL_IP:
        return HttpUtils.getRemoteAddrPreferXRealIp(request);
      case CUSTOM_HEADER:
        StringOptional.ofNullable(HttpUtils.getHeader(request, customHeaderName))
            .orElseGet(() -> HttpUtils.getRemoteAddr(request));
      default:
        return HttpUtils.getRemoteAddr(request);
    }
  }

}
