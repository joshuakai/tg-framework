package com.tg.framework.commons.concurrent.lock;

public interface LockService {

  IdentityLock tryLock(LockContext lockContext) throws LockException;

  void unlock(String key, IdentityLock lock, long delay);

}
