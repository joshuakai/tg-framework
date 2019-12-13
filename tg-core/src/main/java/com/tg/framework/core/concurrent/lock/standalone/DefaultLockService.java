package com.tg.framework.core.concurrent.lock.standalone;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.tg.framework.commons.util.ReflectionUtils;
import com.tg.framework.core.concurrent.lock.LockContext;
import com.tg.framework.core.concurrent.lock.LockService;
import com.tg.framework.core.concurrent.lock.LockTimeoutStrategy;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLockService implements LockService {

  private static final long ENDLESS_TIMEOUT_MILLIS = -1L;

  private static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 5000L;

  private static Logger logger = LoggerFactory.getLogger(DefaultLockService.class);

  private final ConcurrentHashMap<String, LockBean> lockHolder = new ConcurrentHashMap<>();

  private long timeBetweenEvictionRunsMillis;

  public DefaultLockService() {
    this(DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
  }

  public DefaultLockService(long timeBetweenEvictionRunsMillis) {
    this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    startExpireThread();
  }

  @Override
  public boolean tryLock(LockContext lockContext) throws Throwable {
    logger.debug("Try lock {}.", lockContext);
    return tryLock(lockContext, System.currentTimeMillis());
  }

  @Override
  public void unlock(LockContext lockContext) {
    lockHolder.remove(lockContext.getKey());
  }

  private void startExpireThread() {
    new Thread(() -> {
      while (true) {
        logger.debug("Check expired LockBean.");
        Iterator<Entry<String, LockBean>> ite = lockHolder.entrySet().iterator();
        while (ite.hasNext()) {
          Optional.ofNullable(ite.next().getValue())
              .filter(DefaultLockService::isExpired)
              .ifPresent(lb -> {
                logger.debug("Expired LockBean found {}.", lb);
                ite.remove();
              });
        }
        try {
          Thread.sleep(timeBetweenEvictionRunsMillis);
        } catch (InterruptedException e) {
          logger.error("Expire thread was interrupted.", e);
        }
      }
    }).start();
  }

  private boolean tryLock(LockContext lockContext, long enterTime) throws Throwable {
    String key = lockContext.getKey();
    LockBean lockBean = new LockBean(lockContext.getTimeoutMillis());
    if (lockContext.isMutex()) {
      lockBean.setTimestamp(System.currentTimeMillis());
      LockBean previous = lockHolder.putIfAbsent(key, lockBean);
      if (previous == null) {
        logger.debug("Get lock {}.", lockContext);
        return true;
      } else if (isExpired(previous)) {
        logger.debug("Previous lock is expired, retrying.");
        lockHolder.remove(key, previous);
        return tryLock(lockContext, enterTime);
      } else {
        ReflectionUtils
            .throwException(lockContext.getMutexException(), true, new Object[]{lockContext});
      }
    }
    boolean isEndless = lockBean.getTimeout() == ENDLESS_TIMEOUT_MILLIS;
    boolean isTimeout;
    while (isEndless || !(isTimeout =
        enterTime + lockBean.getTimeout() < System.currentTimeMillis())) {
      lockBean.setTimestamp(System.currentTimeMillis());
      LockBean previous;
      if ((previous = lockHolder.putIfAbsent(key, lockBean)) == null) {
        logger.debug("Get lock {}.", lockContext);
        return true;
      } else if (isExpired(previous)) {
        lockHolder.remove(key, previous);
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

  private static boolean isExpired(LockBean lockBean) {
    return lockBean.getTimeout() != ENDLESS_TIMEOUT_MILLIS
        && lockBean.getTimestamp() + lockBean.getTimeout() > System.currentTimeMillis();
  }

  public long getTimeBetweenEvictionRunsMillis() {
    return timeBetweenEvictionRunsMillis;
  }

  public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
    this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
  }

  public static class LockBean {

    private long timestamp;
    private long timeout;

    public LockBean() {
    }

    public LockBean(long timeout) {
      this.timeout = timeout;
    }

    public LockBean(long timestamp, long timeout) {
      this.timestamp = timestamp;
      this.timeout = timeout;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
    }

    public long getTimeout() {
      return timeout;
    }

    public void setTimeout(long timeout) {
      this.timeout = timeout;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      LockBean lockBean = (LockBean) o;
      return timestamp == lockBean.timestamp &&
          timeout == lockBean.timeout;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(timestamp, timeout);
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("timestamp", timestamp)
          .add("timeout", timeout)
          .toString();
    }
  }

}
