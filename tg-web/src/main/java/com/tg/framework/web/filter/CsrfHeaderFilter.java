package com.tg.framework.web.filter;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

public class CsrfHeaderFilter extends OncePerRequestFilter {

  private String cookieName;
  private String cookiePath;

  public CsrfHeaderFilter(String cookieName, String cookiePath) {
    this.cookieName = cookieName;
    this.cookiePath = cookiePath;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    Optional.ofNullable((CsrfToken) request.getAttribute(CsrfToken.class.getName()))
        .ifPresent(csrf -> {
          Cookie cookie = WebUtils.getCookie(request, cookieName);
          String token = csrf.getToken();
          if (cookie == null || token != null && !token.equals(cookie.getValue())) {
            cookie = new Cookie(cookieName, token);
            cookie.setPath(cookiePath);
            response.addCookie(cookie);
          }
        });
    filterChain.doFilter(request, response);
  }
}
