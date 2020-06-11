package com.tg.framework.data.jpa.util;

import com.tg.framework.commons.util.OptionalUtils;

public class SQLUtils {

  private static final String WILDCARD = "%";

  private SQLUtils() {
  }

  public static String startsWith(String str) {
    return OptionalUtils.notEmpty(str).map(s -> s + WILDCARD).orElse(WILDCARD);
  }

  public static String endsWith(String str) {
    return OptionalUtils.notEmpty(str).map(s -> WILDCARD + s).orElse(WILDCARD);
  }

  public static String contains(String str) {
    return OptionalUtils.notEmpty(str).map(s -> WILDCARD + s + WILDCARD).orElse(WILDCARD);
  }

}
