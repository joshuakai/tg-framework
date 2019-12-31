package com.tg.framework.commons.concurrent.lock.standalone;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.tg.framework.commons.concurrent.lock.LockContext;
import com.tg.framework.commons.concurrent.lock.exception.LockException;
import com.tg.framework.commons.concurrent.lock.LockService;
import com.tg.framework.commons.concurrent.lock.LockTimeoutStrategy;
import com.tg.framework.commons.util.ReflectionUtils;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandaloneLockService implements LockService {

  private static final long ENDLESS_TIMEOUT_MILLIS = -1L;

  private static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 5000L;

  private static Logger logger = LoggerFactory.getLogger(StandaloneLockService.class);

  private final ConcurrentHashMap<String, LockBean> lockHolder = new ConcurrentHashMap<>();

  private long timeBetweenEvictionRunsMillis;

  public StandaloneLockService() {
    this(DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
  }

  public StandaloneLockService(long timeBetweenEvictionRunsMillis) {
    this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    startExpireThread();
  }

  @Override
  public Object tryLock(LockContext lockContext) throws LockException {
    logger.debug("Try lock {}.", lockContext);
    return tryLock(lockContext, System.currentTimeMillis());
  }

  @Override
  public void unlock(String key, Object lock) {
    if (lockHolder.get(key) == lock) {
      lockHolder.remove(key);
    }
  }

  private void startExpireThread() {
    Thread thread = new Thread(() -> {
      while (true) {
        logger.debug("Check expired LockBean.");
        Iterator<Entry<String, LockBean>> ite = lockHolder.entrySet().iterator();
        while (ite.hasNext()) {
          Optional.ofNullable(ite.next().getValue())
              .filter(StandaloneLockService::isExpired)
              .ifPresent(lb -> {
                logger.debug("Expired LockBean found {}.", lb);
                ite.remove();
              });
        }
        try {
          Thread.sleep(timeBetweenEvictionRunsMillis);
        } catch (InterruptedException e) {
          logger.error("Expire thread was interrupted.", e);
          break;
        }
      }
    });
    thread.setDaemon(true);
    thread.start();
  }

  private LockBean tryLock(LockContext lockContext, long enterTime) throws LockException {
    String key = lockContext.getKey();
    LockBean lockBean = new LockBean(lockContext.getTimeoutMillis());
    if (lockContext.isMutex()) {
      lockBean.setTimestamp(System.currentTimeMillis());
      LockBean previous = lockHolder.putIfAbsent(key, lockBean);
      if (previous == null) {
        logger.debug("Get lock {}.", lockContext);
        return lockBean;
      } else if (isExpired(previous)) {
        logger.debug("Previous lock is expired, retrying.");
        lockHolder.remove(key, previous);
        return tryLock(lockContext, enterTime);
      } else {
        ReflectionUtils.throwException(lockContext.getMutexException(), true, key);
      }
    }
    boolean isEndless = lockBean.getTimeout() == ENDLESS_TIMEOUT_MILLIS;
    while (isEndless || !(enterTime + lockBean.getTimeout() < System.currentTimeMillis())) {
      lockBean.setTimestamp(System.currentTimeMillis());
      LockBean previous;
      if ((previous = lockHolder.putIfAbsent(key, lockBean)) == null) {
        logger.debug("Get lock {}.", lockContext);
        return lockBean;
      } else if (isExpired(previous)) {
        lockHolder.remove(key, previous);
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
