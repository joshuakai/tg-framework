package com.tg.framework.commons.data.constant;

public enum Switch {

  CLOSE(0), OPEN(1);
  private int value;

  Switch(int value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

}
