package com.tg.framework.core.concurrent.lock.redis;

import com.tg.framework.commons.util.ReflectionUtils;
import com.tg.framework.core.concurrent.lock.LockContext;
import com.tg.framework.core.concurrent.lock.LockService;
import com.tg.framework.core.concurrent.lock.LockTimeoutStrategy;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

public class RedisLockService implements LockService {

  private static Logger logger = LoggerFactory.getLogger(RedisLockService.class);

  private RedisTemplate<String, Long> redisTemplate;

  public RedisLockService(RedisTemplate<String, Long> redisTemplate) {
    Assert.notNull(redisTemplate, "RedisTemplate must not be null.");
    this.redisTemplate = redisTemplate;
  }

  @Override
  public boolean tryLock(LockContext lockContext) throws Throwable {
    logger.debug("Try lock {}.", lockContext);
    return tryLock(lockContext, lockContext.getTimeoutMillis() == -1L, System.currentTimeMillis());
  }

  @Override
  public void unlock(LockContext lockContext) {
    redisTemplate.delete(lockContext.getKey());
  }

  private boolean tryLock(LockContext lockContext, boolean timeoutDisabled, long enterTime)
      throws Throwable {
    long timeoutAt = enterTime + lockContext.getTimeoutMillis();
    boolean isTimeout;
    while (timeoutDisabled || !(isTimeout = timeoutAt < System.currentTimeMillis())) {
      if (redisTemplate.opsForValue().setIfAbsent(lockContext.getKey(), System.currentTimeMillis(),
          lockContext.getTimeoutMillis(), TimeUnit.MILLISECONDS)) {
        logger.debug("Get lock {}.", lockContext);
        return true;
      }
      try {
        Thread.sleep(lockContext.getSleepMillis());
      } catch (InterruptedException e) {
        logger.error("Waiting thread was interrupted.", e);
      }
    }
    if (isTimeout
        && lockContext.getStrategy() == LockTimeoutStrategy.THROW_EXCEPTION_WHILE_TIMEOUT) {
      ReflectionUtils
          .throwException(lockContext.getExceptionClass(), true, new Object[]{lockContext});
    }
    logger.debug("Try lock timeout with strategy {} {}.", LockTimeoutStrategy.RELEASE_WHILE_TIMEOUT,
        lockContext);
    return false;
  }

  public RedisTemplate<String, Long> getRedisTemplate() {
    return redisTemplate;
  }

  public void setRedisTemplate(RedisTemplate<String, Long> redisTemplate) {
    Assert.notNull(redisTemplate, "RedisTemplate must not be null.");
    this.redisTemplate = redisTemplate;
  }
}
