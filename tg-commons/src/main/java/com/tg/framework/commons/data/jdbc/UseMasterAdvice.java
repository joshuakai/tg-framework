package com.tg.framework.commons.data.jdbc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UseMasterAdvice implements MethodInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(UseMasterAdvice.class);

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    boolean changed = DynamicDataSourceLookupKeyHolder.set(DynamicDataSourceLookupKey.MASTER);
    if (changed) {
      LOGGER.debug("Set current lookup key {}.", DynamicDataSourceLookupKey.MASTER);
    }
    try {
      return invocation.proceed();
    } finally {
      if (changed) {
        DynamicDataSourceLookupKeyHolder.remove();
        LOGGER.debug("Remove current lookup key.");
      }
    }
  }
}
