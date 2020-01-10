package com.tg.framework.commons.util;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

public class StreamUtils {

  private StreamUtils() {
  }

  public static <T> Stream<T> safety2Stream(T[] array) {
    return OptionalUtils.notEmpty(array).map(Stream::of).orElseGet(Stream::empty);
  }

  public static <T, C extends Collection<T>> Stream<T> safety2Stream(C collection) {
    return OptionalUtils.notEmpty(collection).map(Collection::stream).orElseGet(Stream::empty);
  }

  public static <K, V, M extends Map<K, V>> Stream<Entry<K, V>> safety2Stream(M map) {
    return OptionalUtils.notEmpty(map).map(Map::entrySet).map(Set::stream).orElseGet(Stream::empty);
  }

  public static <K, V, M extends Map<K, V>> Stream<K> safety2KeyStream(M map) {
    return OptionalUtils.notEmpty(map).map(Map::keySet).map(Set::stream).orElseGet(Stream::empty);
  }

  public static <K, V, M extends Map<K, V>> Stream<V> safety2ValueStream(M map) {
    return OptionalUtils.notEmpty(map).map(Map::values).map(Collection::stream)
        .orElseGet(Stream::empty);
  }

}
