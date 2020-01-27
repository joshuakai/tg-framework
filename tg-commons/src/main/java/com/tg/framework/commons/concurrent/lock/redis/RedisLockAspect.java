package com.tg.framework.commons.concurrent.lock.redis;

import com.tg.framework.commons.concurrent.lock.LockAspectSupport;
import com.tg.framework.commons.concurrent.lock.LockContext;
import java.lang.reflect.Method;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class RedisLockAspect extends LockAspectSupport<RedisLockService> {

  private static final String DEFAULT_KEY_PREFIX = "locks:";
  private static final long DEFAULT_TIMEOUT_MILLIS = -1L;

  private String keyPrefix;
  private long defaultTimeoutMillis;

  public RedisLockAspect(RedisLockService redisLockService) {
    this(redisLockService, DEFAULT_KEY_PREFIX, DEFAULT_TIMEOUT_MILLIS);
  }

  public RedisLockAspect(RedisLockService redisLockService, String keyPrefix) {
    this(redisLockService, keyPrefix, DEFAULT_TIMEOUT_MILLIS);
  }

  public RedisLockAspect(RedisLockService redisLockService, long defaultTimeoutMillis) {
    this(redisLockService, DEFAULT_KEY_PREFIX, defaultTimeoutMillis);
  }

  public RedisLockAspect(RedisLockService redisLockService, String keyPrefix,
      long defaultTimeoutMillis) {
    super(redisLockService);
    this.keyPrefix = keyPrefix;
    this.defaultTimeoutMillis = defaultTimeoutMillis;
  }

  @Around("@annotation(com.tg.framework.commons.concurrent.lock.redis.RedisLock)")
  public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    return doAspect(proceedingJoinPoint);
  }

  @Override
  protected LockContext getLockContext(ProceedingJoinPoint proceedingJoinPoint) {
    Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
    RedisLock lock = method.getAnnotation(RedisLock.class);
    String key = Optional.ofNullable(keyPrefix).map(p -> p + lock.key()).orElse(lock.key());
    key = lock.useExpression() ? getExpressionValue(key, method, proceedingJoinPoint.getArgs(),
        String.class) : key;
    long timeoutMillis =
        lock.timeout() == -1L ? defaultTimeoutMillis : lock.timeUnit().toMillis(lock.timeout());
    return new LockContext(key, lock.mutex(), lock.mutexException(), timeoutMillis,
        lock.timeoutStrategy(), lock.timeoutException(), lock.sleepMillis(), lock.unlockDelay());
  }

  public String getKeyPrefix() {
    return keyPrefix;
  }

  public void setKeyPrefix(String keyPrefix) {
    this.keyPrefix = keyPrefix;
  }

  public long getDefaultTimeoutMillis() {
    return defaultTimeoutMillis;
  }

  public void setDefaultTimeoutMillis(long defaultTimeoutMillis) {
    this.defaultTimeoutMillis = defaultTimeoutMillis;
  }
}
