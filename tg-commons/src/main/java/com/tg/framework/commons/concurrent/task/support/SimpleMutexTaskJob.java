package com.tg.framework.commons.concurrent.task.support;

import com.tg.framework.commons.concurrent.task.MutexTaskJob;
import com.tg.framework.commons.concurrent.task.MutexTaskJobStatus;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class SimpleMutexTaskJob implements MutexTaskJob {

  private String title;
  private Function<Callable<Void>, MutexTaskJobStatus> executor;

  public SimpleMutexTaskJob(String title, Function<Callable<Void>, MutexTaskJobStatus> executor) {
    this.title = title;
    this.executor = executor;
  }

  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public Function<Callable<Void>, MutexTaskJobStatus> getExecutor() {
    return executor;
  }

  public void setExecutor(Function<Callable<Void>, MutexTaskJobStatus> executor) {
    this.executor = executor;
  }

  @Override
  public String toString() {
    return "SimpleMutexTaskJob{" +
        "title='" + title + '\'' +
        ", executor=" + executor +
        '}';
  }
}
