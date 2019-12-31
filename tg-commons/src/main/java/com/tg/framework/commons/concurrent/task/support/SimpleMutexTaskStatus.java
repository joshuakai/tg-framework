package com.tg.framework.commons.concurrent.task.support;

import com.tg.framework.commons.concurrent.task.MutexTaskJobStatus;
import org.apache.commons.lang3.StringUtils;

public class SimpleMutexTaskStatus implements MutexTaskJobStatus {

  private boolean succeed;
  private String message;

  public SimpleMutexTaskStatus() {
  }

  public SimpleMutexTaskStatus(boolean succeed, String message) {
    this.succeed = succeed;
    this.message = message;
  }

  @Override
  public boolean isSucceed() {
    return succeed;
  }

  public void setSucceed(boolean succeed) {
    this.succeed = succeed;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public static SimpleMutexTaskStatus succeed(String message) {
    return new SimpleMutexTaskStatus(true, message);
  }

  public static SimpleMutexTaskStatus fail(String message) {
    return new SimpleMutexTaskStatus(false, message);
  }

  public static SimpleMutexTaskStatus succeed() {
    return succeed(StringUtils.EMPTY);
  }

  public static SimpleMutexTaskStatus fail() {
    return fail(StringUtils.EMPTY);
  }
}
