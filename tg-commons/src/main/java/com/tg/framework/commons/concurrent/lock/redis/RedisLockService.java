package com.tg.framework.commons.concurrent.lock.redis;

import com.tg.framework.commons.concurrent.lock.LockContext;
import com.tg.framework.commons.concurrent.lock.LockService;
import com.tg.framework.commons.concurrent.lock.LockTimeoutStrategy;
import com.tg.framework.commons.concurrent.lock.exception.LockException;
import com.tg.framework.commons.util.ReflectionUtils;
import java.util.Objects;
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
  public Object tryLock(LockContext lockContext) throws LockException {
    logger.debug("Try lock {}.", lockContext);
    return tryLock(lockContext, System.currentTimeMillis());
  }

  @Override
  public void unlock(String key, Object lock, long delay) {
    if (Objects.equals(redisTemplate.opsForValue().get(key), lock)) {
      if (delay > 0L) {
        redisTemplate.expire(key, delay, TimeUnit.MILLISECONDS);
      } else {
        redisTemplate.delete(key);
      }
    }
  }

  private Long tryLock(LockContext lockContext, long enterTime) throws LockException {
    String key = lockContext.getKey();
    if (lockContext.isMutex()) {
      long lock = System.currentTimeMillis();
      if (BooleanUtils.isTrue(redisTemplate.opsForValue()
          .setIfAbsent(key, lock, lockContext.getTimeoutMillis(), TimeUnit.MILLISECONDS))) {
        logger.debug("Get lock {}.", lockContext);
        return lock;
      } else {
        ReflectionUtils.throwException(lockContext.getMutexException(), true, key);
      }
    }
    boolean isEndless = lockContext.getTimeoutMillis() == ENDLESS_TIMEOUT_MILLIS;
    while (isEndless || !(enterTime + lockContext.getTimeoutMillis() < System
        .currentTimeMillis())) {
      long lock = System.currentTimeMillis();
      if (BooleanUtils.isTrue(redisTemplate.opsForValue()
          .setIfAbsent(key, lock, lockContext.getTimeoutMillis(), TimeUnit.MILLISECONDS))) {
        logger.debug("Get lock {}.", lockContext);
        return lock;
      }
      try {
        Thread.sleep(lockContext.getSleepMillis());
      } catch (InterruptedException e) {
        logger.error("Waiting thread was interrupted.", e);
      }
    }
    if (lockContext.getTimeoutStrategy() == LockTimeoutStrategy.THROW_EXCEPTION_WHILE_TIMEOUT) {
      ReflectionUtils.throwException(lockContext.getTimeoutException(), true, key);
    }
    logger.debug("Try lock timeout with strategy {} {}.", LockTimeoutStrategy.RELEASE_WHILE_TIMEOUT,
        lockContext);
    return null;
  }

  public RedisTemplate<String, Long> getRedisTemplate() {
    return redisTemplate;
  }

  public void setRedisTemplate(RedisTemplate<String, Long> redisTemplate) {
    Assert.notNull(redisTemplate, "RedisTemplate must not be null.");
    this.redisTemplate = redisTemplate;
  }
}
