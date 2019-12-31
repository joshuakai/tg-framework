package com.tg.framework.commons.concurrent.task.support;

import com.tg.framework.commons.concurrent.task.MutexTaskJob;
import com.tg.framework.commons.concurrent.task.MutexTaskJobExecutor;

public class SimpleMutexTaskJob implements MutexTaskJob {

  private String title;
  private MutexTaskJobExecutor executor;

  public SimpleMutexTaskJob(String title,
      MutexTaskJobExecutor executor) {
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
  public MutexTaskJobExecutor getExecutor() {
    return executor;
  }

  public void setExecutor(MutexTaskJobExecutor executor) {
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
