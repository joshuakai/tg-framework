package com.tg.framework.commons.data.jdbc;

public class MasterOnlyContext {

  private ThreadLocal<MasterOnlyStatus> threadLocal;

  public MasterOnlyContext() {
    threadLocal = new ThreadLocal<>();
  }

  public boolean mark(boolean masterOnly) {
    MasterOnlyStatus prev = threadLocal.get();
    masterOnly = (prev != null && prev.isMasterOnly()) || masterOnly;
    threadLocal.set(new MasterOnlyStatus(masterOnly, prev));
    return masterOnly;
  }

  public boolean reset() {
    MasterOnlyStatus cur = threadLocal.get();
    MasterOnlyStatus prev = cur != null ? cur.getPrev() : null;
    if (prev != null) {
      threadLocal.set(prev);
      return prev.isMasterOnly();
    } else {
      threadLocal.remove();
      return false;
    }
  }

  public boolean isMasterOnly() {
    MasterOnlyStatus cur = threadLocal.get();
    return cur != null && cur.isMasterOnly();
  }

}
