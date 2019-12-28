package com.tg.framework.beans.domain;

public enum InverseSwitch {

  OPEN(0), CLOSE(1);
  private int value;

  InverseSwitch(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}
