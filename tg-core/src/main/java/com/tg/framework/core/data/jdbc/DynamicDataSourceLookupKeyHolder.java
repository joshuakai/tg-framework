package com.tg.framework.core.data.jdbc;

public class DynamicDataSourceLookupKeyHolder {

  private static final ThreadLocal<DynamicDataSourceLookupKey> THREAD_LOCAL = new ThreadLocal<>();

  public static final void set(DynamicDataSourceLookupKey lookupKey) {
    THREAD_LOCAL.set(lookupKey);
  }

  public static final DynamicDataSourceLookupKey get() {
    return THREAD_LOCAL.get();
  }

  public static final void remove() {
    THREAD_LOCAL.remove();
  }

}
