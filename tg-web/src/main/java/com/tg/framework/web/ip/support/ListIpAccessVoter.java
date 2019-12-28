package com.tg.framework.web.ip.support;

import com.tg.framework.web.ip.IpAccessVoter;
import com.tg.framework.web.util.HttpUtils;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

public class ListIpAccessVoter implements IpAccessVoter {

  private Set<String> ipWhitelist;
  private Set<String> ipBlacklist;
  private boolean ipWhitelistAcceptWildcard;
  private boolean ipBlacklistAcceptWildcard;

  public ListIpAccessVoter(Set<String> ipWhitelist, Set<String> ipBlacklist,
      boolean ipWhitelistAcceptWildcard, boolean ipBlacklistAcceptWildcard) {
    this.ipWhitelist = ipWhitelist;
    this.ipBlacklist = ipBlacklist;
    this.ipWhitelistAcceptWildcard = ipWhitelistAcceptWildcard;
    this.ipBlacklistAcceptWildcard = ipBlacklistAcceptWildcard;
  }

  @Override
  public boolean vote(String ip, HttpServletRequest request) {
    return HttpUtils.isIpAcceptable(ip, ipWhitelist, ipBlacklist, ipWhitelistAcceptWildcard,
        ipBlacklistAcceptWildcard);
  }
}
