package com.tg.framework.commons.concurrent.lock.standalone;

import com.tg.framework.commons.concurrent.lock.LockAspectSupport;
import com.tg.framework.commons.concurrent.lock.LockContext;
import com.tg.framework.commons.concurrent.lock.LockService;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class LockAspect extends LockAspectSupport<LockService> {

  private static final long DEFAULT_TIMEOUT_MILLIS = -1L;

  private long defaultTimeoutMillis;

  public LockAspect() {
    this(new DefaultLockService(), DEFAULT_TIMEOUT_MILLIS);
  }

  public LockAspect(long defaultTimeoutMillis) {
    this(new DefaultLockService(), defaultTimeoutMillis);
  }

  public LockAspect(LockService lockService) {
    this(lockService, DEFAULT_TIMEOUT_MILLIS);
  }

  public LockAspect(LockService lockService, long defaultTimeoutMillis) {
    super(lockService);
    this.defaultTimeoutMillis = defaultTimeoutMillis;
  }

  @Around("@annotation(com.tg.framework.commons.concurrent.lock.standalone.Lock)")
  public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    return doAspect(proceedingJoinPoint);
  }

  protected LockContext getLockContext(ProceedingJoinPoint proceedingJoinPoint) {
    Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
    Lock lock = method.getAnnotation(Lock.class);
    String key =
        lock.useExpression() ? getExpressionValue(lock.key(), method, proceedingJoinPoint.getArgs(),
            String.class) : lock.key();
    long timeoutMillis =
        lock.timeout() == -1L ? defaultTimeoutMillis : lock.timeUnit().toMillis(lock.timeout());
    return new LockContext(key, lock.mutex(), lock.mutexException(), timeoutMillis,
        lock.timeoutStrategy(), lock.timeoutException(), lock.sleepMillis());
  }

  public long getDefaultTimeoutMillis() {
    return defaultTimeoutMillis;
  }

  public void setDefaultTimeoutMillis(long defaultTimeoutMillis) {
    this.defaultTimeoutMillis = defaultTimeoutMillis;
  }
}
