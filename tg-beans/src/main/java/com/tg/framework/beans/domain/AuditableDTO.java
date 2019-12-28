package com.tg.framework.beans.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

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
    AuditableDTO<?> that = (AuditableDTO<?>) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "AuditableDTO{" +
        "id=" + id +
        ", createdAt=" + createdAt +
        ", createdBy='" + createdBy + '\'' +
        ", lastModifiedAt=" + lastModifiedAt +
        ", lastModifiedBy='" + lastModifiedBy + '\'' +
        '}';
  }
}
