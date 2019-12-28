package com.tg.framework.web.ip;

import javax.servlet.http.HttpServletRequest;

public interface IpAccessVoter {

  boolean vote(String ip, HttpServletRequest request);

}
