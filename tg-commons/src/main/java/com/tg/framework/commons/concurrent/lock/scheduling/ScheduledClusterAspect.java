package com.tg.framework.commons.concurrent.lock.scheduling;

import com.tg.framework.commons.concurrent.lock.IdentityLock;
import com.tg.framework.commons.concurrent.lock.LockContext;
import com.tg.framework.commons.concurrent.lock.exception.LockMutexException;
import com.tg.framework.commons.concurrent.lock.exception.LockTimeoutException;
import com.tg.framework.commons.concurrent.lock.redis.RedisLockService;
import com.tg.framework.commons.expression.AbstractExpressionAspect;
import java.lang.reflect.Method;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.Assert;

@Aspect
public class ScheduledClusterAspect extends AbstractExpressionAspect {

  private static final String DEFAULT_KEY_PREFIX = "scheduled_locks:";

  private RedisLockService redisLockService;
  private String keyPrefix;

  public ScheduledClusterAspect(RedisLockService redisLockService) {
    this(redisLockService, DEFAULT_KEY_PREFIX);
  }

  public ScheduledClusterAspect(RedisLockService redisLockService, String keyPrefix) {
    Assert.notNull(redisLockService, "RedisLockService must not be null.");
    Assert.isTrue(StringUtils.isNotBlank(keyPrefix), "Key prefix must not be empty.");
    this.redisLockService = redisLockService;
    this.keyPrefix = keyPrefix;
  }

  @Around("@annotation(com.tg.framework.commons.concurrent.lock.scheduling.ScheduledCluster)")
  public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
    Assert.isTrue(method.getReturnType() == Void.TYPE,
        "Annotation @ScheduledCluster could be only used with methods which returns void.");
    ScheduledCluster scheduledCluster = method.getAnnotation(ScheduledCluster.class);
    LockContext context = getLockContext(method, proceedingJoinPoint.getArgs(), scheduledCluster);
    IdentityLock lock;
    try {
      lock = redisLockService.tryLock(context);
    } catch (LockMutexException e) {
      return null;
    }
    try {
      return proceedingJoinPoint.proceed();
    } finally {
      redisLockService.unlock(context.getKey(), lock, scheduledCluster.releaseDelay());
    }
  }

  private LockContext getLockContext(Method method, Object[] args,
      ScheduledCluster scheduledCluster) {
    String key = Optional.ofNullable(keyPrefix).map(p -> p + scheduledCluster.key())
        .orElse(scheduledCluster.key());
    key = scheduledCluster.useExpression() ? getExpressionValue(key, method, args, String.class)
        : key;
    return new LockContext(key, true, LockMutexException.class, -1L, LockTimeoutException.class,
        0L);
  }
}
