package com.tg.framework.commons.data.jdbc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UseSlaveAdvice implements MethodInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(UseSlaveAdvice.class);

  private DynamicDataSourceContext context;

  public UseSlaveAdvice(DynamicDataSourceContext context) {
    this.context = context;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    boolean changed = false;
    if (context.get() == null) {
      DynamicDataSourceLookupKey lookupKey = DynamicDataSourceLookupKey.SLAVE;
      context.set(lookupKey);
      LOGGER.debug("Set current lookup key {}.", lookupKey);
      changed = true;
    }
    try {
      return invocation.proceed();
    } finally {
      if (changed) {
        context.remove();
        LOGGER.debug("Remove current lookup key.");
      }
    }
  }
}
