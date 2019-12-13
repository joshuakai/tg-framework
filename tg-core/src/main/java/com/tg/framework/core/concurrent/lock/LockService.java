package com.tg.framework.core.concurrent.lock;

public interface LockService {

  boolean tryLock(LockContext lockContext) throws Throwable;

  void unlock(LockContext lockContext);

  static LockContext lockToDeath(String key) {
    return new LockContext(key, false, LockMutexException.class, -1L,
        LockTimeoutStrategy.THROW_EXCEPTION_WHILE_TIMEOUT, LockTimeoutException.class, 100L);
  }

}
