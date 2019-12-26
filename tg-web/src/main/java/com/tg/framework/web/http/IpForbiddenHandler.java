package com.tg.framework.web.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface IpForbiddenHandler {

  boolean onIpForbidden(HttpServletRequest request, HttpServletResponse response, String ip)
      throws IOException, ServletException;

}
