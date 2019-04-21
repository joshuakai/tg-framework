package com.tg.framework.core.concurrent.lock;

import com.tg.framework.core.AbstractExpressionAspect;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.util.Assert;

public abstract class LockAspectSupport<T extends LockService> extends AbstractExpressionAspect {

  protected T lockService;
  protected final Map<Method, Expression> expressionCache = new ConcurrentHashMap<>();
  protected final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

  public LockAspectSupport(T lockService) {
    Assert.notNull(lockService, "LockService must not be null.");
    this.lockService = lockService;
  }

  protected Object doAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    LockContext lockContext = getLockContext(proceedingJoinPoint);
    boolean isLockGot = lockService.tryLock(lockContext);
    try {
      return proceedingJoinPoint.proceed();
    } finally {
      if (isLockGot) {
        lockService.unlock(lockContext);
      }
    }
  }

  protected abstract LockContext getLockContext(ProceedingJoinPoint proceedingJoinPoint);

  public T getLockService() {
    return lockService;
  }

  public void setLockService(T lockService) {
    Assert.notNull(lockService, "LockService must not be null.");
    this.lockService = lockService;
  }
}
