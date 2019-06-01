package com.tg.framework.commons.data.constant;

public enum InverseSwitch {

  OPEN(0), CLOSE(1);
  private int value;

  InverseSwitch(int value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

}
