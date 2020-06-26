package com.tg.framework.commons.concurrent.lock.redis;

import com.tg.framework.commons.FrameworkMessageSource;
import com.tg.framework.commons.concurrent.lock.IdentityLock;
import com.tg.framework.commons.concurrent.lock.LockContext;
import com.tg.framework.commons.concurrent.lock.LockService;
import com.tg.framework.commons.concurrent.lock.LockException;
import com.tg.framework.commons.concurrent.lock.LockMutexException;
import com.tg.framework.commons.concurrent.lock.LockTimeoutException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

public class RedisLockService implements LockService, MessageSourceAware {

  private static Logger logger = LoggerFactory.getLogger(RedisLockService.class);

  private static final long ENDLESS_TIMEOUT_MILLIS = -1L;

  private RedisTemplate<String, IdentityLock> redisTemplate;
  private String instanceId;
  private MessageSourceAccessor messages = FrameworkMessageSource.getAccessor();

  public RedisLockService(RedisTemplate<String, IdentityLock> redisTemplate) {
    Assert.notNull(redisTemplate, "A redis template must be set");
    this.redisTemplate = redisTemplate;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  @Override
  public void setMessageSource(MessageSource messageSource) {
    Assert.notNull(messageSource, "A message source must be set");
    this.messages = new MessageSourceAccessor(messageSource);
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
        throw new LockMutexException(key, resolveMessage(lockContext.getMessage()));
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
    throw new LockTimeoutException(key, resolveMessage(lockContext.getMessage()));
  }

  private String resolveMessage(String message) {
    return messages.getMessage(message, message);
  }
}
