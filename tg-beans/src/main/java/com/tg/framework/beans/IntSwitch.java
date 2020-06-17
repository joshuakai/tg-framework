package com.tg.framework.beans;

public enum IntSwitch {

  CLOSE(0), OPEN(1);
  private int value;

  IntSwitch(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public boolean matches(Integer value) {
    return value != null && this.value == value;
  }

}
