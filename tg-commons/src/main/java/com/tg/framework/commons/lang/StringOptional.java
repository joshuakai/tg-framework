package com.tg.framework.commons.lang;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class StringOptional {

  private StringOptional() {
  }

  public static Optional<String> of(String value) {
    return Optional.of(value).filter(StringUtils::isNotBlank);
  }

  public static Optional<String> ofNullable(String value) {
    return Optional.ofNullable(value).filter(StringUtils::isNotBlank);
  }

}
