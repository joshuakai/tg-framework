package com.tg.framework.core.concurrent.task;

import com.tg.framework.commons.util.JavaTimeUtils;
import java.math.BigDecimal;
import java.util.Optional;

public interface UniqueTaskService {

  long SINGLE_PROGRESS_STEP = 1L;

  UniqueTask get(String key);

  long start(String key, String title, long totalSteps, String startBy, long historyKeepMillis);

  long progress(String key, long id, long progressSteps, UniqueTaskStep... steps);

  default long progress(String key, long id, UniqueTaskStep step) {
    return progress(key, id, SINGLE_PROGRESS_STEP, step);
  }

  static int sortUniqueTaskStep(UniqueTaskStep a, UniqueTaskStep b) {
    long c = Optional.ofNullable(a).map(UniqueTaskStep::getCompletedAt)
        .map(JavaTimeUtils::milliseconds).orElse(0L);
    long d = Optional.ofNullable(b).map(UniqueTaskStep::getCompletedAt)
        .map(JavaTimeUtils::milliseconds).orElse(0L);
    return BigDecimal.valueOf(c - d).intValue();
  }

}
