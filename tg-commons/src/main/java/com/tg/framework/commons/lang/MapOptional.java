package com.tg.framework.commons.lang;

import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.MapUtils;

public class MapOptional {

  private MapOptional() {
  }

  public static <K, V, M extends Map<K, V>> Optional<M> of(M value) {
    return Optional.of(value).filter(MapUtils::isNotEmpty);
  }

  public static <K, V, M extends Map<K, V>> Optional<M> ofNullable(M value) {
    return Optional.ofNullable(value).filter(MapUtils::isNotEmpty);
  }

}
