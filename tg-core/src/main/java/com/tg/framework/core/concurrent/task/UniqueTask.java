package com.tg.framework.core.concurrent.task;

import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class UniqueTask implements Serializable {

  private static final long serialVersionUID = 4775408872713187034L;

  private String key;
  private long id;
  private String title;
  private long totalSteps;
  private LocalDateTime startAt;
  private String startBy;
  private long completedSteps;
  private List<UniqueTaskStep> steps;
  private LocalDateTime completedAt;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public long getTotalSteps() {
    return totalSteps;
  }

  public void setTotalSteps(long totalSteps) {
    this.totalSteps = totalSteps;
  }

  public LocalDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(LocalDateTime startAt) {
    this.startAt = startAt;
  }

  public String getStartBy() {
    return startBy;
  }

  public void setStartBy(String startBy) {
    this.startBy = startBy;
  }

  public long getCompletedSteps() {
    return completedSteps;
  }

  public void setCompletedSteps(long completedSteps) {
    this.completedSteps = completedSteps;
  }

  public List<UniqueTaskStep> getSteps() {
    return steps;
  }

  public void setSteps(List<UniqueTaskStep> steps) {
    this.steps = steps;
  }

  public LocalDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(LocalDateTime completedAt) {
    this.completedAt = completedAt;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("key", key)
        .add("id", id)
        .add("title", title)
        .add("totalSteps", totalSteps)
        .add("startAt", startAt)
        .add("startBy", startBy)
        .add("completedSteps", completedSteps)
        .add("steps", steps)
        .add("completedAt", completedAt)
        .toString();
  }
}
