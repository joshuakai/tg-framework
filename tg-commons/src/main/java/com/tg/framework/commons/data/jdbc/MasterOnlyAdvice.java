package com.tg.framework.commons.data.jdbc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MasterOnlyAdvice implements MethodInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(MasterOnlyAdvice.class);

  private MasterOnlyContext context;

  public MasterOnlyAdvice(MasterOnlyContext context) {
    this.context = context;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    boolean masterOnly = context.mark(true);
    LOGGER.debug("Mark master-only as {}.", masterOnly);
    try {
      return invocation.proceed();
    } finally {
      masterOnly = context.reset();
      LOGGER.debug("Reset master-only as {}.", masterOnly);
    }
  }
}
