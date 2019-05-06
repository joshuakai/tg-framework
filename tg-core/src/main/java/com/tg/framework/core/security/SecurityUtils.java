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

  public static Optional<Object> getPrincipal(Authentication authentication) {
    return Optional.ofNullable(authentication).map(Authentication::getPrincipal);
  }

  public static <T> Optional<T> getPrincipal(Class<T> clazz) {
    return safetyMap(getPrincipal(), clazz);
  }

  public static <T> Optional<T> getPrincipal(Authentication authentication, Class<T> clazz) {
    return safetyMap(getPrincipal(authentication), clazz);
  }

  private static <T> Optional<T> safetyMap(Optional<Object> optional, Class<T> clazz) {
    return optional.filter(p -> clazz.isAssignableFrom(p.getClass())).map(p -> (T) p);
  }

}
