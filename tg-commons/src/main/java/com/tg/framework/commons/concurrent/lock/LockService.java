package com.tg.framework.commons.concurrent.lock;

import com.tg.framework.commons.concurrent.lock.exception.LockException;
import com.tg.framework.commons.concurrent.lock.exception.LockMutexException;
import com.tg.framework.commons.concurrent.lock.exception.LockTimeoutException;

public interface LockService {

  Object tryLock(LockContext lockContext) throws LockException;

  void unlock(String key, Object lock, long delay);

  static LockContext lockToDeath(String key) {
    return new LockContext(key, false, LockMutexException.class, -1L,
        LockTimeoutStrategy.THROW_EXCEPTION_WHILE_TIMEOUT, LockTimeoutException.class, 100L, 0L);
  }

}
