package com.tg.framework.data.commons.masterslave.context;

public class MasterOnlyStatus {

  private boolean masterOnly;
  private MasterOnlyStatus prev;

  public MasterOnlyStatus(boolean masterOnly,
      MasterOnlyStatus prev) {
    this.masterOnly = masterOnly;
    this.prev = prev;
  }

  public boolean isMasterOnly() {
    return masterOnly;
  }

  public void setMasterOnly(boolean masterOnly) {
    this.masterOnly = masterOnly;
  }

  public MasterOnlyStatus getPrev() {
    return prev;
  }

  public void setPrev(MasterOnlyStatus prev) {
    this.prev = prev;
  }
}
