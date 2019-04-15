package com.tg.framework.core.security;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

  private SecurityUtils() {
  }

  public static Optional<Authentication> getAuthentication() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication);
  }

  public static Optional<Object> getPrincipal() {
    return getAuthentication().map(Authentication::getPrincipal);
  }

}
