package com.tg.framework.core.data.jdbc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UseSlaveAdvice implements MethodInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(UseSlaveAdvice.class);

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    DynamicDataSourceLookupKeyHolder.set(DynamicDataSourceLookupKey.SLAVE);
    LOGGER.debug("Set current lookup key {}.", DynamicDataSourceLookupKey.SLAVE);
    try {
      return invocation.proceed();
    } finally {
      DynamicDataSourceLookupKeyHolder.remove();
      LOGGER.debug("Remove current lookup key.");
    }
  }
}
