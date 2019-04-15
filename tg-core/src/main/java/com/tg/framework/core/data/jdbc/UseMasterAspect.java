package com.tg.framework.core.data.jdbc;

import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.core.Ordered;

public class UseMasterAspect extends AspectJExpressionPointcutAdvisor {

  public UseMasterAspect(String expression) {
    setExpression(expression);
    setOrder(Ordered.HIGHEST_PRECEDENCE);
    setAdvice(new UseMasterAdvice());
  }
}
