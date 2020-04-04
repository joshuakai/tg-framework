package com.tg.framework.web.ip.support;

import com.tg.framework.web.ip.RequestDetailsResolver;
import com.tg.framework.web.util.HttpUtils;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;

public class XForwardedRequestDetailsResolver implements RequestDetailsResolver {

  private Set<String> proxyWhitelist;
  private boolean proxyWhitelistAcceptWildcard;
  private boolean considerNoneAsTrustAll;

  public XForwardedRequestDetailsResolver(Set<String> proxyWhitelist,
      boolean proxyWhitelistAcceptWildcard, boolean considerNoneAsTrustAll) {
    this.proxyWhitelist = proxyWhitelist;
    this.proxyWhitelistAcceptWildcard = proxyWhitelistAcceptWildcard;
    this.considerNoneAsTrustAll = considerNoneAsTrustAll;
  }

  private boolean isProxyTrusted(String ip) {
    if (HttpUtils.isLocalhost(ip)) {
      return true;
    }
    boolean isProxyWhitelistAssigned = CollectionUtils.isNotEmpty(proxyWhitelist);
    return HttpUtils.isLocalhost(ip) || (!isProxyWhitelistAssigned && considerNoneAsTrustAll) || (
        isProxyWhitelistAssigned && HttpUtils
            .isIpInWhitelist(ip, proxyWhitelist, proxyWhitelistAcceptWildcard));
  }

  @Override
  public String resolveRemoteAddr(HttpServletRequest request) {
    String remoteAddr = HttpUtils.getRemoteAddr(request);
    if (!isProxyTrusted(remoteAddr)) {
      return remoteAddr;
    }
    return HttpUtils.getRemoteAddrPreferXForwardedFor(request);
  }

  @Override
  public String resolveProtocol(HttpServletRequest request) {
    String remoteAddr = HttpUtils.getRemoteAddr(request);
    if (!isProxyTrusted(remoteAddr)) {
      return HttpUtils.getProtocol(request);
    }
    return HttpUtils.getProtocolPreferXForwardedProto(request);
  }

  @Override
  public String resolveHost(HttpServletRequest request) {
    String remoteAddr = HttpUtils.getRemoteAddr(request);
    if (!isProxyTrusted(remoteAddr)) {
      return HttpUtils.getHost(request);
    }
    return HttpUtils.getHostPreferXForwardedHost(request);
  }

  @Override
  public int resolvePort(HttpServletRequest request) {
    String remoteAddr = HttpUtils.getRemoteAddr(request);
    if (!isProxyTrusted(remoteAddr)) {
      return HttpUtils.getPort(request);
    }
    return HttpUtils.getPortPreferXForwardedPort(request);
  }

  @Override
  public String resolveRequestURI(HttpServletRequest request) {
    String remoteAddr = HttpUtils.getRemoteAddr(request);
    if (!isProxyTrusted(remoteAddr)) {
      return HttpUtils.getRequestURI(request);
    }
    return HttpUtils.getRequestURIPreferXForwardedPrefix(request);
  }

  @Override
  public String resolveUrl(HttpServletRequest request) {
    String remoteAddr = HttpUtils.getRemoteAddr(request);
    if (!isProxyTrusted(remoteAddr)) {
      return HttpUtils.getUrl(request);
    }
    return HttpUtils.getUrlPreferXForwarded(request);
  }

}
