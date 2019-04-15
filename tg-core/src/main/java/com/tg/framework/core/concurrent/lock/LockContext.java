package com.tg.framework.core.concurrent.lock;

import com.google.common.base.MoreObjects;

public class LockContext {

  private String key;
  private long timeoutMillis;
  private LockTimeoutStrategy strategy;
  private Class<? extends Throwable> exceptionClass;
  private long sleepMillis;

  public LockContext() {
  }

  public LockContext(String key, long timeoutMillis, LockTimeoutStrategy strategy,
      Class<? extends Throwable> exceptionClass, long sleepMillis) {
    this.key = key;
    this.timeoutMillis = timeoutMillis;
    this.strategy = strategy;
    this.exceptionClass = exceptionClass;
    this.sleepMillis = sleepMillis;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public long getTimeoutMillis() {
    return timeoutMillis;
  }

  public void setTimeoutMillis(long timeoutMillis) {
    this.timeoutMillis = timeoutMillis;
  }

  public LockTimeoutStrategy getStrategy() {
    return strategy;
  }

  public void setStrategy(LockTimeoutStrategy strategy) {
    this.strategy = strategy;
  }

  public Class<? extends Throwable> getExceptionClass() {
    return exceptionClass;
  }

  public void setExceptionClass(Class<? extends Throwable> exceptionClass) {
    this.exceptionClass = exceptionClass;
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
        .add("timeoutMillis", timeoutMillis)
        .add("strategy", strategy)
        .add("exceptionClass", exceptionClass)
        .add("sleepMillis", sleepMillis)
        .toString();
  }
}
