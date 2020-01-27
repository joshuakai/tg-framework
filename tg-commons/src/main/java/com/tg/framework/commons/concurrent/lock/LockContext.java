package com.tg.framework.commons.concurrent.lock;

import com.tg.framework.commons.concurrent.lock.exception.LockMutexException;
import com.tg.framework.commons.concurrent.lock.exception.LockTimeoutException;

public class LockContext {

  private String key;
  private boolean mutex;
  private Class<? extends LockMutexException> mutexException;
  private long timeoutMillis;
  private LockTimeoutStrategy timeoutStrategy;
  private Class<? extends LockTimeoutException> timeoutException;
  private long sleepMillis;
  private long unlockDelay;

  public LockContext() {
  }

  public LockContext(String key, boolean mutex, Class<? extends LockMutexException> mutexException,
      long timeoutMillis, LockTimeoutStrategy timeoutStrategy,
      Class<? extends LockTimeoutException> timeoutException, long sleepMillis,
      long unlockDelay) {
    this.key = key;
    this.mutex = mutex;
    this.mutexException = mutexException;
    this.timeoutMillis = timeoutMillis;
    this.timeoutStrategy = timeoutStrategy;
    this.timeoutException = timeoutException;
    this.sleepMillis = sleepMillis;
    this.unlockDelay = unlockDelay;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public boolean isMutex() {
    return mutex;
  }

  public void setMutex(boolean mutex) {
    this.mutex = mutex;
  }

  public Class<? extends LockMutexException> getMutexException() {
    return mutexException;
  }

  public void setMutexException(
      Class<? extends LockMutexException> mutexException) {
    this.mutexException = mutexException;
  }

  public long getTimeoutMillis() {
    return timeoutMillis;
  }

  public void setTimeoutMillis(long timeoutMillis) {
    this.timeoutMillis = timeoutMillis;
  }

  public LockTimeoutStrategy getTimeoutStrategy() {
    return timeoutStrategy;
  }

  public void setTimeoutStrategy(
      LockTimeoutStrategy timeoutStrategy) {
    this.timeoutStrategy = timeoutStrategy;
  }

  public Class<? extends LockTimeoutException> getTimeoutException() {
    return timeoutException;
  }

  public void setTimeoutException(
      Class<? extends LockTimeoutException> timeoutException) {
    this.timeoutException = timeoutException;
  }

  public long getSleepMillis() {
    return sleepMillis;
  }

  public void setSleepMillis(long sleepMillis) {
    this.sleepMillis = sleepMillis;
  }

  public long getUnlockDelay() {
    return unlockDelay;
  }

  public void setUnlockDelay(long unlockDelay) {
    this.unlockDelay = unlockDelay;
  }

  @Override
  public String toString() {
    return "LockContext{" +
        "key='" + key + '\'' +
        ", mutex=" + mutex +
        ", mutexException=" + mutexException +
        ", timeoutMillis=" + timeoutMillis +
        ", timeoutStrategy=" + timeoutStrategy +
        ", timeoutException=" + timeoutException +
        ", sleepMillis=" + sleepMillis +
        ", unlockDelay=" + unlockDelay +
        '}';
  }
}
