package com.tg.framework.commons.security.util;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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

  private static String mapPrincipalAsString(Object principal) {
    if (principal instanceof UserDetails) {
      return ((UserDetails) principal).getUsername();
    }
    return principal.toString();
  }

  public static Optional<String> getPrincipalAsString() {
    return getAuthentication().map(Authentication::getPrincipal).map(SecurityUtils::mapPrincipalAsString);
  }

  public static Optional<String> getPrincipalAsString(Authentication authentication) {
    return Optional.ofNullable(authentication).map(Authentication::getPrincipal).map(SecurityUtils::mapPrincipalAsString);
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
