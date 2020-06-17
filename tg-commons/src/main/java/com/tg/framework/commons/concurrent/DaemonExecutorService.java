package com.tg.framework.commons.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.util.Assert;

public class DaemonExecutorService extends ThreadPoolExecutor {

  public DaemonExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime,
      TimeUnit unit, BlockingQueue<Runnable> workQueue) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
        new DaemonThreadFactory(Executors.defaultThreadFactory()));
  }

  public DaemonExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime,
      TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
        new DaemonThreadFactory(threadFactory));
  }

  public DaemonExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime,
      TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
        new DaemonThreadFactory(Executors.defaultThreadFactory()), handler);
  }

  public DaemonExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime,
      TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
        new DaemonThreadFactory(threadFactory), handler);
  }

  private static class DaemonThreadFactory implements ThreadFactory {

    private ThreadFactory delegated;

    public DaemonThreadFactory(ThreadFactory delegated) {
      Assert.notNull(delegated, "A delegated thread factory must be set");
      this.delegated = delegated;
    }

    @Override
    public Thread newThread(Runnable r) {
      Thread thread = delegated.newThread(r);
      thread.setDaemon(true);
      return thread;
    }
  }
}
