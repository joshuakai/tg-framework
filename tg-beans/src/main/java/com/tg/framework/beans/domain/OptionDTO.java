package com.tg.framework.beans.domain;

import java.io.Serializable;
import java.util.Objects;

public class OptionDTO<T> implements Serializable {

  private static final long serialVersionUID = 6781155077082155777L;

  public OptionDTO() {
  }

  public OptionDTO(T value, String text) {
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
    OptionDTO<?> optionDTO = (OptionDTO<?>) o;
    return Objects.equals(value, optionDTO.value);
  }

  @Override
  public int hashCode() {

    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "OptionDTO{" +
        "value=" + value +
        ", text='" + text + '\'' +
        '}';
  }
}
