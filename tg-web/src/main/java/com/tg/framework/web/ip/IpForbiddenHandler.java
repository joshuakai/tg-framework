package com.tg.framework.web.ip;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

@FunctionalInterface
public interface IpForbiddenHandler {

  boolean onIpForbidden(HttpServletRequest request, HttpServletResponse response, String ip)
      throws IOException, ServletException;

  IpForbiddenHandler DEFAULT = (request, response, ip) -> {
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.getWriter().print(ip);
    response.flushBuffer();
    return false;
  };

}
