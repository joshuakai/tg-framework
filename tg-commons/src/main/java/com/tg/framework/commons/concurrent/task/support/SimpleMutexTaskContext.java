package com.tg.framework.commons.concurrent.task.support;

import com.tg.framework.commons.concurrent.task.MutexTaskContext;
import com.tg.framework.commons.concurrent.task.MutexTaskJob;
import java.util.List;

public class SimpleMutexTaskContext implements MutexTaskContext {

  private List<MutexTaskJob> preparingJobs;
  private List<MutexTaskJob> mainJobs;
  private List<MutexTaskJob> finishingJobs;

  @Override
  public List<MutexTaskJob> getPreparingJobs() {
    return preparingJobs;
  }

  public void setPreparingJobs(
      List<MutexTaskJob> preparingJobs) {
    this.preparingJobs = preparingJobs;
  }

  @Override
  public List<MutexTaskJob> getMainJobs() {
    return mainJobs;
  }

  public void setMainJobs(List<MutexTaskJob> mainJobs) {
    this.mainJobs = mainJobs;
  }

  @Override
  public List<MutexTaskJob> getFinishingJobs() {
    return finishingJobs;
  }

  public void setFinishingJobs(
      List<MutexTaskJob> finishingJobs) {
    this.finishingJobs = finishingJobs;
  }
}
