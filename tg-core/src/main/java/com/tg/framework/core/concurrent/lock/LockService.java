package com.tg.framework.core.concurrent.lock;

public interface LockService {

  boolean tryLock(LockContext lockContext) throws Throwable;

  void unlock(LockContext lockContext);

}
