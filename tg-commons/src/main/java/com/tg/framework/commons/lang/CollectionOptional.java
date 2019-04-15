package com.tg.framework.commons.lang;

import java.util.Collection;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;

public class CollectionOptional {

  private CollectionOptional() {
  }

  public static <E, C extends Collection<E>> Optional<C> of(C value) {
    return Optional.of(value).filter(CollectionUtils::isNotEmpty);
  }

  public static <E, C extends Collection<E>> Optional<C> ofNullable(C value) {
    return Optional.ofNullable(value).filter(CollectionUtils::isNotEmpty);
  }

}
