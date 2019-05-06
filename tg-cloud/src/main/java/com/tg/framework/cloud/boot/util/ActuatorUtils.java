package com.tg.framework.cloud.boot.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class ActuatorUtils {

  private ActuatorUtils() {
  }

  public static RequestMatcher defaultExpose(String basePath) {
    return new OrRequestMatcher(
        Stream.of("/info", "/health").map(p -> basePath + p).map(AntPathRequestMatcher::new)
            .collect(Collectors.toList()));
  }

}
