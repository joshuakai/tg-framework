package com.tg.framework.web.ip.support;

import com.tg.framework.web.ip.RequestDetailsResolver;
import com.tg.framework.web.util.HttpUtils;
import javax.servlet.http.HttpServletRequest;

public class DefaultRequestDetailsResolver implements RequestDetailsResolver {

  @Override
  public String resolveRemoteAddr(HttpServletRequest request) {
    return HttpUtils.getRemoteAddr(request);
  }

  @Override
  public String resolveProtocol(HttpServletRequest request) {
    return HttpUtils.getProtocol(request);
  }

  @Override
  public String resolveHost(HttpServletRequest request) {
    return HttpUtils.getHost(request);
  }

  @Override
  public int resolvePort(HttpServletRequest request) {
    return HttpUtils.getPort(request);
  }

  @Override
  public String resolveRequestURI(HttpServletRequest request) {
    return HttpUtils.getRequestURI(request);
  }

  @Override
  public String resolveUrl(HttpServletRequest request) {
    return HttpUtils.getUrl(request);
  }
}
