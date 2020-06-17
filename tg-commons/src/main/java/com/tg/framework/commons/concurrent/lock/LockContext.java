package com.tg.framework.commons.concurrent.lock;

public class LockContext {

  private String key;
  private boolean mutex;
  private long timeoutMillis;
  private long sleepMillis;
  private String message;

  public LockContext() {
  }

  public LockContext(String key, boolean mutex, long timeoutMillis, long sleepMillis,
      String message) {
    this.key = key;
    this.mutex = mutex;
    this.timeoutMillis = timeoutMillis;
    this.sleepMillis = sleepMillis;
    this.message = message;
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

  public long getTimeoutMillis() {
    return timeoutMillis;
  }

  public void setTimeoutMillis(long timeoutMillis) {
    this.timeoutMillis = timeoutMillis;
  }

  public long getSleepMillis() {
    return sleepMillis;
  }

  public void setSleepMillis(long sleepMillis) {
    this.sleepMillis = sleepMillis;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
