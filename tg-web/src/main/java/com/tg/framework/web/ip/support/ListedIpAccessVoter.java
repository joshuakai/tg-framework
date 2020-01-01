package com.tg.framework.web.ip.support;

import com.tg.framework.web.ip.IpAccessVoter;
import com.tg.framework.web.util.HttpUtils;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

public class ListedIpAccessVoter implements IpAccessVoter {

  private Set<String> ipWhitelist;
  private Set<String> ipBlacklist;
  private boolean ipWhitelistAcceptWildcard;
  private boolean ipBlacklistAcceptWildcard;

  @Override
  public boolean vote(String ip, HttpServletRequest request) {
    return HttpUtils.isLocalhost(ip) || HttpUtils
        .isIpAcceptable(ip, ipWhitelist, ipBlacklist, ipWhitelistAcceptWildcard,
            ipBlacklistAcceptWildcard);
  }

  public Set<String> getIpWhitelist() {
    return ipWhitelist;
  }

  public void setIpWhitelist(Set<String> ipWhitelist) {
    this.ipWhitelist = ipWhitelist;
  }

  public Set<String> getIpBlacklist() {
    return ipBlacklist;
  }

  public void setIpBlacklist(Set<String> ipBlacklist) {
    this.ipBlacklist = ipBlacklist;
  }

  public boolean isIpWhitelistAcceptWildcard() {
    return ipWhitelistAcceptWildcard;
  }

  public void setIpWhitelistAcceptWildcard(boolean ipWhitelistAcceptWildcard) {
    this.ipWhitelistAcceptWildcard = ipWhitelistAcceptWildcard;
  }

  public boolean isIpBlacklistAcceptWildcard() {
    return ipBlacklistAcceptWildcard;
  }

  public void setIpBlacklistAcceptWildcard(boolean ipBlacklistAcceptWildcard) {
    this.ipBlacklistAcceptWildcard = ipBlacklistAcceptWildcard;
  }
}
