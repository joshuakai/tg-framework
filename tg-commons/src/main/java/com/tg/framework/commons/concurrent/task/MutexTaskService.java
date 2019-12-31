package com.tg.framework.commons.concurrent.task;

import com.tg.framework.commons.concurrent.task.exception.TaskMutexException;
import java.util.concurrent.CompletableFuture;

public interface MutexTaskService {

  Long tryStarting(String key, String title, String startedBy, long historyKeepMillis)
      throws TaskMutexException;

  MutexTask get(String key);

  MutexTask start(String key, Long lock, MutexTaskContext context) throws TaskMutexException;

  CompletableFuture<MutexTask> startAsync(String key, Long lock, MutexTaskContext context) throws TaskMutexException;

  boolean forceRelease(String key);

}
