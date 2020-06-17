package com.tg.framework.commons.util;

import java.security.Principal;
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
    if (principal instanceof Principal) {
      return ((Principal) principal).getName();
    }
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
    return getPrincipal().flatMap(p -> safetyCast(p, clazz));
  }

  public static <T> Optional<T> getPrincipal(Authentication authentication, Class<T> clazz) {
    return getPrincipal(authentication).flatMap(p -> safetyCast(p, clazz));
  }

  private static <T> Optional<T> safetyCast(Object object, Class<T> clazz) {
    //noinspection unchecked
    return Optional.of(object).filter(o -> clazz.isAssignableFrom(o.getClass())).map(p -> (T) p);
  }

}
