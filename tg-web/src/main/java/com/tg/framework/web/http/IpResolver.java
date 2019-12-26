package com.tg.framework.web.http;

import javax.servlet.http.HttpServletRequest;

public interface IpResolver {

  String resolve(HttpServletRequest request);

}
