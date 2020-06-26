package com.tg.framework.commons.concurrent.task;

import com.tg.framework.commons.concurrent.lock.IdentityLock;
import java.util.concurrent.CompletableFuture;

public interface MutexTaskService {

  IdentityLock tryStarting(String key, String title, String startedBy, long historyKeepMillis)
      throws TaskMutexException;

  MutexTask get(String key);

  MutexTask start(String key, IdentityLock lock, MutexTaskContext context)
      throws TaskMutexException;

  CompletableFuture<MutexTask> startAsync(String key, IdentityLock lock, MutexTaskContext context)
      throws TaskMutexException;

}
