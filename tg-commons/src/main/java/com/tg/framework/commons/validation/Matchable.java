package com.tg.framework.commons.validation;

public interface Matchable<T> {

  T getValue();

  default boolean matches(T value) {
    return value != null && value.equals(getValue());
  }

}
