package com.tg.framework.web.ip;

import javax.servlet.http.HttpServletRequest;

public interface RequestDetailsResolver {

  String resolveRemoteAddr(HttpServletRequest request);

  String resolveProtocol(HttpServletRequest request);

  String resolveHost(HttpServletRequest request);

  int resolvePort(HttpServletRequest request);

  String resolveRequestURI(HttpServletRequest request);

  String resolveUrl(HttpServletRequest request);

}
