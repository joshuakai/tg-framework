package com.tg.framework.commons.concurrent.task.support;

import com.tg.framework.commons.concurrent.task.MutexTask;
import java.time.LocalDateTime;
import java.util.List;

public class SimpleMutexTask implements MutexTask {

  private static final long serialVersionUID = -9221700319217729461L;

  private String key;
  private Long lock;
  private String title;
  private String startedBy;
  private LocalDateTime startedAt;
  private String executeNode;
  private int totalSteps;
  private int finishedSteps;
  private boolean started;
  private boolean stopped;
  private boolean succeed;
  private LocalDateTime stoppedAt;
  private List<SimpleMutexTaskJobResult> results;

  @Override
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Override
  public Long getLock() {
    return lock;
  }

  public void setLock(Long lock) {
    this.lock = lock;
  }

  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getStartedBy() {
    return startedBy;
  }

  public void setStartedBy(String startedBy) {
    this.startedBy = startedBy;
  }

  @Override
  public LocalDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(LocalDateTime startedAt) {
    this.startedAt = startedAt;
  }

  @Override
  public String getExecuteNode() {
    return executeNode;
  }

  public void setExecuteNode(String executeNode) {
    this.executeNode = executeNode;
  }

  @Override
  public int getTotalSteps() {
    return totalSteps;
  }

  public void setTotalSteps(int totalSteps) {
    this.totalSteps = totalSteps;
  }

  @Override
  public int getFinishedSteps() {
    return finishedSteps;
  }

  public void setFinishedSteps(int finishedSteps) {
    this.finishedSteps = finishedSteps;
  }

  @Override
  public boolean isStarted() {
    return started;
  }

  public void setStarted(boolean started) {
    this.started = started;
  }

  @Override
  public boolean isStopped() {
    return stopped;
  }

  public void setStopped(boolean stopped) {
    this.stopped = stopped;
  }

  @Override
  public boolean isSucceed() {
    return succeed;
  }

  public void setSucceed(boolean succeed) {
    this.succeed = succeed;
  }

  @Override
  public LocalDateTime getStoppedAt() {
    return stoppedAt;
  }

  public void setStoppedAt(LocalDateTime stoppedAt) {
    this.stoppedAt = stoppedAt;
  }

  @Override
  public List<SimpleMutexTaskJobResult> getResults() {
    return results;
  }

  public void setResults(
      List<SimpleMutexTaskJobResult> results) {
    this.results = results;
  }
}
