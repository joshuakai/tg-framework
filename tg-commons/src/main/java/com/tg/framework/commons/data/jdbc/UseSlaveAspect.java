package com.tg.framework.commons.data.jdbc;

import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.core.Ordered;

public class UseSlaveAspect extends AspectJExpressionPointcutAdvisor {

  public UseSlaveAspect(String expression, DynamicDataSourceContext context) {
    setExpression(expression);
    setOrder(Ordered.HIGHEST_PRECEDENCE);
    setAdvice(new UseSlaveAdvice(context));
  }
}
