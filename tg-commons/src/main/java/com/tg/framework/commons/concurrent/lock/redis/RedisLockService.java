package com.tg.framework.commons.concurrent.lock.redis;

import com.tg.framework.commons.concurrent.lock.IdentityLock;
import com.tg.framework.commons.concurrent.lock.LockContext;
import com.tg.framework.commons.concurrent.lock.LockService;
import com.tg.framework.commons.concurrent.lock.exception.LockException;
import com.tg.framework.commons.concurrent.lock.exception.LockMutexException;
import com.tg.framework.commons.concurrent.lock.exception.LockTimeoutException;
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

  private RedisTemplate<String, IdentityLock> redisTemplate;
  private String instanceId;

  public RedisLockService(RedisTemplate<String, IdentityLock> redisTemplate) {
    Assert.notNull(redisTemplate, "RedisTemplate must not be null.");
    this.redisTemplate = redisTemplate;
  }

  @Override
  public IdentityLock tryLock(LockContext lockContext) throws LockException {
    logger.debug("Try lock {}.", lockContext);
    return tryLock(lockContext, System.currentTimeMillis());
  }

  @Override
  public void unlock(String key, IdentityLock lock, long delay) {
    if (Objects.equals(redisTemplate.opsForValue().get(key), lock)) {
      if (delay > 0L) {
        redisTemplate.expire(key, delay, TimeUnit.MILLISECONDS);
      } else {
        redisTemplate.delete(key);
      }
    }
  }

  private IdentityLock tryLock(LockContext lockContext, long enterTime) throws LockException {
    String key = lockContext.getKey();
    if (lockContext.isMutex()) {
      IdentityLock lock = new IdentityLock(System.currentTimeMillis(), instanceId);
      if (BooleanUtils.isTrue(redisTemplate.opsForValue()
          .setIfAbsent(key, lock, lockContext.getTimeoutMillis(), TimeUnit.MILLISECONDS))) {
        logger.debug("Get lock {}.", lockContext);
        return lock;
      } else {
        Class<? extends LockMutexException> clazz = lockContext.getMutexException();
        if (clazz == LockMutexException.class) {
          throw new LockMutexException(key);
        }
        ReflectionUtils.throwException(clazz, true, key);
        return null;
      }
    }
    boolean isEndless = lockContext.getTimeoutMillis() == ENDLESS_TIMEOUT_MILLIS;
    while (isEndless || !(enterTime + lockContext.getTimeoutMillis() < System
        .currentTimeMillis())) {
      IdentityLock lock = new IdentityLock(System.currentTimeMillis(), instanceId);
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
    Class<? extends LockTimeoutException> clazz = lockContext.getTimeoutException();
    if (clazz == LockTimeoutException.class) {
      throw new LockTimeoutException(key);
    }
    ReflectionUtils.throwException(clazz, true, key);
    return null;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }
}
