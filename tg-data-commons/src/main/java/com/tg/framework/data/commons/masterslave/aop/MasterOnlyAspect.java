package com.tg.framework.data.commons.masterslave.aop;

import com.tg.framework.data.commons.masterslave.context.MasterOnlyContext;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.core.Ordered;

public class MasterOnlyAspect extends AspectJExpressionPointcutAdvisor {

  public MasterOnlyAspect(String expression, MasterOnlyContext context) {
    setExpression(expression);
    setOrder(Ordered.HIGHEST_PRECEDENCE);
    setAdvice(new MasterOnlyAdvice(context));
  }
}
