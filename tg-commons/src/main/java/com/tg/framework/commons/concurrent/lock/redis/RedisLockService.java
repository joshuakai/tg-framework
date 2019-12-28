package com.tg.framework.commons.concurrent.lock.redis;

import com.tg.framework.commons.util.ReflectionUtils;
import com.tg.framework.commons.concurrent.lock.LockContext;
import com.tg.framework.commons.concurrent.lock.LockService;
import com.tg.framework.commons.concurrent.lock.LockTimeoutStrategy;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

public class RedisLockService implements LockService {

  private static Logger logger = LoggerFactory.getLogger(RedisLockService.class);

  private static final long ENDLESS_TIMEOUT_MILLIS = -1L;

  private RedisTemplate<String, Long> redisTemplate;

  public RedisLockService(RedisTemplate<String, Long> redisTemplate) {
    Assert.notNull(redisTemplate, "RedisTemplate must not be null.");
    this.redisTemplate = redisTemplate;
  }

  @Override
  public boolean tryLock(LockContext lockContext) throws Throwable {
    logger.debug("Try lock {}.", lockContext);
    return tryLock(lockContext, System.currentTimeMillis());
  }

  @Override
  public void unlock(LockContext lockContext) {
    redisTemplate.delete(lockContext.getKey());
  }

  private boolean tryLock(LockContext lockContext, long enterTime)
      throws Throwable {
    String key = lockContext.getKey();
    if (lockContext.isMutex()) {
      if (BooleanUtils.isTrue(redisTemplate.opsForValue()
          .setIfAbsent(key, System.currentTimeMillis(), lockContext.getTimeoutMillis(),
              TimeUnit.MILLISECONDS))) {
        logger.debug("Get lock {}.", lockContext);
        return true;
      } else {
        ReflectionUtils
            .throwException(lockContext.getMutexException(), true, new Object[]{lockContext});
      }
    }
    boolean isEndless = lockContext.getTimeoutMillis() == ENDLESS_TIMEOUT_MILLIS;
    boolean isTimeout;
    while (isEndless || !(isTimeout =
        enterTime + lockContext.getTimeoutMillis() < System.currentTimeMillis())) {
      if (BooleanUtils.isTrue(redisTemplate.opsForValue()
          .setIfAbsent(key, System.currentTimeMillis(), lockContext.getTimeoutMillis(),
              TimeUnit.MILLISECONDS))) {
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
        && lockContext.getTimeoutStrategy() == LockTimeoutStrategy.THROW_EXCEPTION_WHILE_TIMEOUT) {
      ReflectionUtils
          .throwException(lockContext.getTimeoutException(), true, new Object[]{lockContext});
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
