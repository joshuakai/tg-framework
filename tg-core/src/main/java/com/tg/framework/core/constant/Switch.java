package com.tg.framework.core.constant;

import com.tg.framework.commons.validation.Matchable;

public enum Switch implements Matchable<Integer> {

  CLOSE(0), OPEN(1);
  private int value;

  Switch(int value) {
    this.value = value;
  }

  @Override
  public Integer getValue() {
    return value;
  }

}
