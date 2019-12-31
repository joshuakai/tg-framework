package com.tg.framework.commons.concurrent.task;

import com.tg.framework.commons.util.JavaTimeUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MutexTaskJobResult {

  String getTitle();

  LocalDateTime getStartedAt();

  boolean isStarted();

  boolean isStopped();

  boolean isSucceed();

  String getMessage();

  LocalDateTime getStoppedAt();

  static int sort(MutexTaskJobResult a, MutexTaskJobResult b) {
    long c = Optional.ofNullable(a).map(MutexTaskJobResult::getStartedAt)
        .map(JavaTimeUtils::milliseconds).orElse(0L);
    long d = Optional.ofNullable(b).map(MutexTaskJobResult::getStartedAt)
        .map(JavaTimeUtils::milliseconds).orElse(0L);
    return BigDecimal.valueOf(c - d).intValue();
  }

}
