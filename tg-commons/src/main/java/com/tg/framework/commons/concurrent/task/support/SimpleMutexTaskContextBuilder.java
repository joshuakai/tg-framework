package com.tg.framework.commons.concurrent.task.support;

import com.tg.framework.commons.concurrent.task.MutexTaskJob;
import com.tg.framework.commons.concurrent.task.MutexTaskJobExecutor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SimpleMutexTaskContextBuilder {

  private List<MutexTaskJob> preparingJobs;
  private List<MutexTaskJob> mainJobs;
  private List<MutexTaskJob> finishingJobs;

  private SimpleMutexTaskContextBuilder() {
  }

  public static SimpleMutexTaskContextBuilder aSimpleMutexTaskContext() {
    return new SimpleMutexTaskContextBuilder();
  }

  public SimpleMutexTaskContextBuilder withPreparingJobs(List<MutexTaskJob> preparingJobs) {
    this.preparingJobs = preparingJobs;
    return this;
  }

  public SimpleMutexTaskContextBuilder withMainJobs(List<MutexTaskJob> mainJobs) {
    this.mainJobs = mainJobs;
    return this;
  }

  public SimpleMutexTaskContextBuilder withFinishingJobs(List<MutexTaskJob> finishingJobs) {
    this.finishingJobs = finishingJobs;
    return this;
  }

  public SimpleMutexTaskContextBuilder withSinglePreparingJob(MutexTaskJob preparingJob) {
    this.preparingJobs = Stream.of(preparingJob).collect(Collectors.toList());
    return this;
  }

  public SimpleMutexTaskContextBuilder withSingleMainJob(MutexTaskJob mainJob) {
    this.mainJobs = Stream.of(mainJob).collect(Collectors.toList());
    return this;
  }

  public SimpleMutexTaskContextBuilder withSingleFinishingJob(MutexTaskJob finishingJob) {
    this.finishingJobs = Stream.of(finishingJob).collect(Collectors.toList());
    return this;
  }

  public SimpleMutexTaskContextBuilder appendPreparingJob(MutexTaskJob preparingJob) {
    if (this.preparingJobs == null) {
      this.preparingJobs = new ArrayList<>();
    }
    this.preparingJobs.add(preparingJob);
    return this;
  }

  public SimpleMutexTaskContextBuilder appendMainJob(MutexTaskJob mainJob) {
    if (this.mainJobs == null) {
      this.mainJobs = new ArrayList<>();
    }
    this.mainJobs.add(mainJob);
    return this;
  }

  public SimpleMutexTaskContextBuilder appendFinishingJob(MutexTaskJob finishingJob) {
    if (this.finishingJobs == null) {
      this.finishingJobs = new ArrayList<>();
    }
    this.finishingJobs.add(finishingJob);
    return this;
  }

  public SimpleMutexTaskContextBuilder appendPreparingJob(String title,
      MutexTaskJobExecutor executor) {
    return appendPreparingJob(new SimpleMutexTaskJob(title, executor));
  }

  public SimpleMutexTaskContextBuilder appendMainJob(String title, MutexTaskJobExecutor executor) {
    return appendMainJob(new SimpleMutexTaskJob(title, executor));
  }

  public SimpleMutexTaskContextBuilder appendFinishingJob(String title,
      MutexTaskJobExecutor executor) {
    return appendFinishingJob(new SimpleMutexTaskJob(title, executor));
  }

  public SimpleMutexTaskContext build() {
    SimpleMutexTaskContext simpleMutexTaskContext = new SimpleMutexTaskContext();
    simpleMutexTaskContext.setPreparingJobs(preparingJobs);
    simpleMutexTaskContext.setMainJobs(mainJobs);
    simpleMutexTaskContext.setFinishingJobs(finishingJobs);
    return simpleMutexTaskContext;
  }
}
