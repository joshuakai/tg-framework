package com.tg.framework.commons.concurrent.task;

import java.util.concurrent.Callable;
import java.util.function.Function;

public interface MutexTaskJob {

  String getTitle();

  Function<Callable<Void>, MutexTaskJobStatus> getExecutor();

}
