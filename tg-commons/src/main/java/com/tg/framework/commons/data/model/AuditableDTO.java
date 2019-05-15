package com.tg.framework.commons.data.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class AuditableDTO<ID extends Serializable> implements Serializable {

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuditableDTO that = (AuditableDTO) o;
    return Objects.equal(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("createdAt", createdAt)
        .add("createdBy", createdBy)
        .add("lastModifiedAt", lastModifiedAt)
        .add("lastModifiedBy", lastModifiedBy)
        .toString();
  }
}
