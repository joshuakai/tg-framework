package com.tg.framework.commons.data.jdbc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UseSlaveAdvice implements MethodInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(UseSlaveAdvice.class);

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    boolean changed = DynamicDataSourceLookupKeyHolder.set(DynamicDataSourceLookupKey.SLAVE);
    if (changed) {
      LOGGER.debug("Set current lookup key {}.", DynamicDataSourceLookupKey.SLAVE);
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
