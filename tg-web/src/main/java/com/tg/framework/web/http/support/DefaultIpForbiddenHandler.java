package com.tg.framework.web.http.support;

import com.tg.framework.web.http.IpForbiddenHandler;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public class DefaultIpForbiddenHandler implements IpForbiddenHandler {

  @Override
  public boolean onIpForbidden(HttpServletRequest request, HttpServletResponse response, String ip)
      throws IOException {
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.getWriter().print(String.format("%s is not allowed to access.", ip));
    response.flushBuffer();
    return false;
  }
}
