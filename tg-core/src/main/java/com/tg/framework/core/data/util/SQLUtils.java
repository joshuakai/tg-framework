package com.tg.framework.core.data.util;

import com.tg.framework.commons.lang.StringOptional;

public class SQLUtils {

  private static final String WILDCARD = "%";

  private SQLUtils() {
  }

  public static String startsWith(String str) {
    return StringOptional.ofNullable(str).map(s -> s + WILDCARD).orElse(WILDCARD);
  }

  public static String endsWith(String str) {
    return StringOptional.ofNullable(str).map(s -> WILDCARD + s).orElse(WILDCARD);
  }

  public static String contains(String str) {
    return StringOptional.ofNullable(str).map(s -> WILDCARD + s + WILDCARD).orElse(WILDCARD);
  }

}
