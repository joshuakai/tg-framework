package com.tg.framework.commons.concurrent.task;

public interface MutexTaskJob {

  String getTitle();

  MutexTaskJobExecutor getExecutor();

}
