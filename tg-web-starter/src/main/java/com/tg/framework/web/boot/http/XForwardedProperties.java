package com.tg.framework.web.boot.http;

import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tg.web.x-forwarded")
public class XForwardedProperties {

  private Set<String> proxyWhitelist;
  private boolean proxyWhitelistAcceptWildcard;
  private boolean considerNoneAsTrustAll;

  public Set<String> getProxyWhitelist() {
    return proxyWhitelist;
  }

  public void setProxyWhitelist(Set<String> proxyWhitelist) {
    this.proxyWhitelist = proxyWhitelist;
  }

  public boolean isProxyWhitelistAcceptWildcard() {
    return proxyWhitelistAcceptWildcard;
  }

  public void setProxyWhitelistAcceptWildcard(boolean proxyWhitelistAcceptWildcard) {
    this.proxyWhitelistAcceptWildcard = proxyWhitelistAcceptWildcard;
  }

  public boolean isConsiderNoneAsTrustAll() {
    return considerNoneAsTrustAll;
  }

  public void setConsiderNoneAsTrustAll(boolean considerNoneAsTrustAll) {
    this.considerNoneAsTrustAll = considerNoneAsTrustAll;
  }
}
