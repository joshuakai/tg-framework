package com.tg.framework.commons.data.jdbc;

public class DynamicDataSourceLookupKeyHolder {

  private static final ThreadLocal<DynamicDataSourceLookupKey> THREAD_LOCAL = new ThreadLocal<>();

  public static boolean set(DynamicDataSourceLookupKey lookupKey) {
    if (THREAD_LOCAL.get() == null) {
      THREAD_LOCAL.set(lookupKey);
      return true;
    }
    return false;
  }

  public static DynamicDataSourceLookupKey get() {
    return THREAD_LOCAL.get();
  }

  public static void remove() {
    THREAD_LOCAL.remove();
  }

}
