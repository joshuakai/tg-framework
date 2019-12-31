package com.tg.framework.commons.concurrent.task;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface MutexTaskJobExecutor {

  MutexTaskJobStatus execute(Callable canceller);

}
