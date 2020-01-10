package com.tg.framework.commons.util;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class OptionalUtils {

  private OptionalUtils() {
  }

  public static Optional<String> notEmpty(String value, boolean notBlank) {
    if (notBlank) {
      return Optional.ofNullable(value).filter(StringUtils::isNotBlank);
    }
    return Optional.ofNullable(value).filter(StringUtils::isNotEmpty);
  }

  public static Optional<String> notEmpty(String value) {
    return notEmpty(value, true);
  }

  public static <T> Optional<T[]> notEmpty(T[] array) {
    return Optional.ofNullable(array).filter(arr -> arr.length != 0);
  }

  public static <T, C extends Collection<T>> Optional<C> notEmpty(C collection) {
    return Optional.ofNullable(collection).filter(col -> !col.isEmpty());
  }

  public static <K, V, M extends Map<K, V>> Optional<M> notEmpty(M map) {
    return Optional.ofNullable(map).filter(m -> !m.isEmpty());
  }

}
