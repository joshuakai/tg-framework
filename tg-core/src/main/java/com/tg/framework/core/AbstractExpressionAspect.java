package com.tg.framework.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public abstract class AbstractExpressionAspect {

  protected final Map<Method, Expression> expressionCache = new ConcurrentHashMap<>();
  protected final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

  protected Object getExpressionValue(String expressionString, Method method, Object[] args) {
    return getExpression(method, expressionString)
        .getValue(new MethodBasedEvaluationContext(null, method, args, discoverer));
  }

  protected <T> T getExpressionValue(String expressionString, Method method, Object[] args,
      Class<T> clazz) {
    return getExpression(method, expressionString)
        .getValue(new MethodBasedEvaluationContext(null, method, args, discoverer), clazz);
  }

  private Expression getExpression(Method method, String expressionString) {
    return Optional.ofNullable(expressionCache.get(method)).orElseGet(() -> {
      ExpressionParser parser = new SpelExpressionParser();
      Expression exp = parser.parseExpression(expressionString);
      if (expressionCache.putIfAbsent(method, exp) != null) {
        return expressionCache.get(method);
      }
      return exp;
    });
  }

}
