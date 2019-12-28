package com.tg.framework.commons.data.jdbc;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DynamicDataSourceAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

  @Around("@annotation(com.tg.framework.commons.data.jdbc.UseDataSource)")
  public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    UseDataSource useDataSource = method.getAnnotation(UseDataSource.class);
    DynamicDataSourceLookupKey lookupKey = useDataSource.value();
    DynamicDataSourceLookupKeyHolder.set(lookupKey);
    LOGGER.debug("Set current lookup key {}.", lookupKey);
    try {
      return pjp.proceed();
    } finally {
      DynamicDataSourceLookupKeyHolder.remove();
      LOGGER.debug("Remove current lookup key.");
    }
  }

}
