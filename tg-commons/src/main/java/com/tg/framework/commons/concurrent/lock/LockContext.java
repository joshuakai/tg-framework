package com.tg.framework.commons.concurrent.lock;

import com.google.common.base.MoreObjects;

public class LockContext {

  private String key;
  private boolean mutex;
  private Class<? extends Throwable> mutexException;
  private long timeoutMillis;
  private LockTimeoutStrategy timeoutStrategy;
  private Class<? extends Throwable> timeoutException;
  private long sleepMillis;

  public LockContext() {
  }

  public LockContext(String key, boolean mutex,
      Class<? extends Throwable> mutexException, long timeoutMillis,
      LockTimeoutStrategy timeoutStrategy,
      Class<? extends Throwable> timeoutException, long sleepMillis) {
    this.key = key;
    this.mutex = mutex;
    this.mutexException = mutexException;
    this.timeoutMillis = timeoutMillis;
    this.timeoutStrategy = timeoutStrategy;
    this.timeoutException = timeoutException;
    this.sleepMillis = sleepMillis;
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

  public Class<? extends Throwable> getMutexException() {
    return mutexException;
  }

  public void setMutexException(Class<? extends Throwable> mutexException) {
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

  public void setTimeoutStrategy(LockTimeoutStrategy timeoutStrategy) {
    this.timeoutStrategy = timeoutStrategy;
  }

  public Class<? extends Throwable> getTimeoutException() {
    return timeoutException;
  }

  public void setTimeoutException(Class<? extends Throwable> timeoutException) {
    this.timeoutException = timeoutException;
  }

  public long getSleepMillis() {
    return sleepMillis;
  }

  public void setSleepMillis(long sleepMillis) {
    this.sleepMillis = sleepMillis;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("key", key)
        .add("mutex", mutex)
        .add("mutexException", mutexException)
        .add("timeoutMillis", timeoutMillis)
        .add("timeoutStrategy", timeoutStrategy)
        .add("timeoutException", timeoutException)
        .add("sleepMillis", sleepMillis)
        .toString();
  }
}
