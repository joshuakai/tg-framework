package com.tg.framework.commons.concurrent.task;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public interface MutexTask extends Serializable {

  String getKey();

  Long getLock();

  String getTitle();

  String getStartedBy();

  LocalDateTime getStartedAt();

  int getTotalSteps();

  int getFinishedSteps();

  boolean isStarted();

  boolean isStopped();

  boolean isSucceed();

  LocalDateTime getStoppedAt();

  List<? extends MutexTaskJobResult> getResults();

}
