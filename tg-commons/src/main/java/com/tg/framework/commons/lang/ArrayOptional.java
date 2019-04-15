package com.tg.framework.commons.lang;

import java.util.Optional;
import org.apache.commons.lang3.ArrayUtils;

public class ArrayOptional {

  private ArrayOptional() {
  }

  public static <T> Optional<T[]> of(T[] value) {
    return Optional.of(value).filter(ArrayUtils::isNotEmpty);
  }

  public static <T> Optional<T[]> ofNullable(T[] value) {
    return Optional.ofNullable(value).filter(ArrayUtils::isNotEmpty);
  }

}
