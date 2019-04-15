package com.tg.framework.web.util;

import com.tg.framework.commons.lang.StringOptional;
import org.apache.commons.lang3.StringUtils;

public class WebSecurityUtils {

  private static final String ANT_PATTERN_ROOT = "/";
  private static final String ANT_PATTERN_BASE_PATH_SUFFIX = "/**";

  private WebSecurityUtils() {
  }

  public static String antPatternOfBasePath(String basePath) {
    return StringOptional.ofNullable(basePath)
        .filter(p -> !StringUtils.equals(p, ANT_PATTERN_ROOT))
        .map(p -> basePath + ANT_PATTERN_BASE_PATH_SUFFIX)
        .orElse(ANT_PATTERN_BASE_PATH_SUFFIX);
  }

}
