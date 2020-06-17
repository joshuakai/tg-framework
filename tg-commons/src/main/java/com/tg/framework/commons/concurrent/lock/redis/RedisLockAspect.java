package com.tg.framework.commons.concurrent.lock.redis;

import com.tg.framework.commons.concurrent.AbstractExpressionAspect;
import com.tg.framework.commons.concurrent.lock.IdentityLock;
import com.tg.framework.commons.concurrent.lock.LockContext;
import java.lang.reflect.Method;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.Assert;

@Aspect
public class RedisLockAspect extends AbstractExpressionAspect {

  private static final String DEFAULT_KEY_PREFIX = "locks:";
  private static final long DEFAULT_TIMEOUT_MILLIS = -1L;

  private RedisLockService redisLockService;
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
    Assert.notNull(redisLockService, "A redis lock service must bet set");
    Assert.hasText(keyPrefix, "Key prefix must not be null or empty");
    Assert.isTrue(defaultTimeoutMillis >= DEFAULT_TIMEOUT_MILLIS,
        "Default timeout millis must be greater than -1");
    this.redisLockService = redisLockService;
    this.keyPrefix = keyPrefix;
    this.defaultTimeoutMillis = defaultTimeoutMillis;
  }

  @Around("@annotation(com.tg.framework.commons.concurrent.lock.redis.RedisLock)")
  public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
    RedisLock redisLock = method.getAnnotation(RedisLock.class);
    LockContext context = getLockContext(method, proceedingJoinPoint.getArgs(), redisLock);
    IdentityLock lock = redisLockService.tryLock(context);
    try {
      return proceedingJoinPoint.proceed();
    } finally {
      redisLockService.unlock(context.getKey(), lock, redisLock.releaseDelay());
    }
  }

  private LockContext getLockContext(Method method, Object[] args, RedisLock redisLock) {
    String key = Optional.ofNullable(keyPrefix).map(p -> p + redisLock.key())
        .orElse(redisLock.key());
    key = redisLock.useExpression() ? getExpressionValue(key, method, args, String.class) : key;
    long timeoutMillis = redisLock.timeout() == -1L ? defaultTimeoutMillis
        : redisLock.timeUnit().toMillis(redisLock.timeout());
    return new LockContext(key, redisLock.mutex(), timeoutMillis, redisLock.sleepMillis(),
        redisLock.message());
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
