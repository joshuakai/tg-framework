package com.tg.framework.beans;

import java.io.Serializable;
import java.util.Objects;

public class ValueTextPair<T> implements Serializable {

  private static final long serialVersionUID = 6781155077082155777L;

  public ValueTextPair() {
  }

  public ValueTextPair(T value, String text) {
    this.value = value;
    this.text = text;
  }

  private T value;
  private String text;

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValueTextPair<?> that = (ValueTextPair<?>) o;
    return value.equals(that.value) &&
        text.equals(that.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, text);
  }

  @Override
  public String toString() {
    return "ValueTextPair{" +
        "value=" + value +
        ", text='" + text + '\'' +
        '}';
  }
}
