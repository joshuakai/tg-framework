package com.tg.framework.core.data.jdbc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UseMasterAdvice implements MethodInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(UseMasterAdvice.class);

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    DynamicDataSourceLookupKeyHolder.set(DynamicDataSourceLookupKey.MASTER);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Set current lookup key {}.", DynamicDataSourceLookupKey.MASTER);
    }
    try {
      return invocation.proceed();
    } finally {
      DynamicDataSourceLookupKeyHolder.remove();
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Remove current lookup key.");
      }
    }
  }
}
