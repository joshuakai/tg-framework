package com.tg.framework.commons.concurrent.lock;

import com.tg.framework.commons.concurrent.lock.exception.LockMutexException;
import com.tg.framework.commons.concurrent.lock.exception.LockTimeoutException;

public class LockContext {

  private String key;
  private boolean mutex;
  private Class<? extends LockMutexException> mutexException;
  private long timeoutMillis;
  private Class<? extends LockTimeoutException> timeoutException;
  private long sleepMillis;

  public LockContext() {
  }

  public LockContext(String key, boolean mutex, Class<? extends LockMutexException> mutexException,
      long timeoutMillis, Class<? extends LockTimeoutException> timeoutException,
      long sleepMillis) {
    this.key = key;
    this.mutex = mutex;
    this.mutexException = mutexException;
    this.timeoutMillis = timeoutMillis;
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

  @Override
  public String toString() {
    return "LockContext{" +
        "key='" + key + '\'' +
        ", mutex=" + mutex +
        ", mutexException=" + mutexException +
        ", timeoutMillis=" + timeoutMillis +
        ", timeoutException=" + timeoutException +
        ", sleepMillis=" + sleepMillis +
        '}';
  }
}
