package com.tg.framework.web.ip;

import javax.servlet.http.HttpServletRequest;

public interface IpResolver {

  String resolve(HttpServletRequest request);

}
