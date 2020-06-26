package com.tg.framework.beans;

public enum ReverseIntSwitch {

  OPEN(0), CLOSE(1);
  private int value;

  ReverseIntSwitch(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}
