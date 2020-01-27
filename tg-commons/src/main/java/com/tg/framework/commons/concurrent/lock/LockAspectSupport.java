package com.tg.framework.commons.concurrent.lock;

import com.tg.framework.commons.expression.AbstractExpressionAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.Assert;

public abstract class LockAspectSupport<T extends LockService> extends AbstractExpressionAspect {

  protected T lockService;

  public LockAspectSupport(T lockService) {
    Assert.notNull(lockService, "LockService must not be null.");
    this.lockService = lockService;
  }

  protected Object doAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    LockContext context = getLockContext(proceedingJoinPoint);
    Object lock = lockService.tryLock(context);
    try {
      return proceedingJoinPoint.proceed();
    } finally {
      lockService.unlock(context.getKey(), lock, context.getUnlockDelay());
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
