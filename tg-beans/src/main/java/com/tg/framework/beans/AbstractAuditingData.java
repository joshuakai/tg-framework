package com.tg.framework.beans;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class AbstractAuditingData<ID extends Serializable> implements Serializable {

  private static final long serialVersionUID = -5388306031879399375L;

  private ID id;
  private LocalDateTime createdAt;
  private String createdBy;
  private LocalDateTime lastModifiedAt;
  private String lastModifiedBy;

  public ID getId() {
    return id;
  }

  public void setId(ID id) {
    this.id = id;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getLastModifiedAt() {
    return lastModifiedAt;
  }

  public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
    this.lastModifiedAt = lastModifiedAt;
  }

  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

}
