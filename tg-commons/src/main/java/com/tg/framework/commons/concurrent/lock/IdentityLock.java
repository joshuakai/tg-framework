package com.tg.framework.commons.concurrent.lock;

import java.io.Serializable;
import java.util.Objects;

public class IdentityLock implements Serializable {

  private static final long serialVersionUID = -7065569081812332507L;

  private Long value;
  private String instanceId;

  public IdentityLock() {
  }

  public IdentityLock(Long value) {
    this.value = value;
  }

  public IdentityLock(Long value, String instanceId) {
    this.value = value;
    this.instanceId = instanceId;
  }

  public Long getValue() {
    return value;
  }

  public void setValue(Long value) {
    this.value = value;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IdentityLock that = (IdentityLock) o;
    return Objects.equals(value, that.value) &&
        Objects.equals(instanceId, that.instanceId);
  }

  @Override
  public int hashCode() {

    return Objects.hash(value, instanceId);
  }

  @Override
  public String toString() {
    return "IdentityLock{" +
        "value=" + value +
        ", instanceId='" + instanceId + '\'' +
        '}';
  }
}
