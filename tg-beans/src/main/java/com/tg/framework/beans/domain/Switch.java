package com.tg.framework.beans.domain;

public enum Switch {

  CLOSE(0), OPEN(1);
  private int value;

  Switch(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}
