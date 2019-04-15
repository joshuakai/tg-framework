package com.tg.framework.core.data.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity extends AbstractEntity {

  private static final long serialVersionUID = -6822940944657705278L;

  @Column(nullable = false)
  @CreatedDate
  protected LocalDateTime createdAt;
  @Column(nullable = false, length = 50)
  @CreatedBy
  protected String createdBy;
  @Column(nullable = false)
  @LastModifiedDate
  protected LocalDateTime lastModifiedAt;
  @Column(nullable = false, length = 50)
  @LastModifiedBy
  protected String lastModifiedBy;

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
  public String toString() {
    return com.google.common.base.MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("createdAt", createdAt)
        .add("createdBy", createdBy)
        .add("lastModifiedAt", lastModifiedAt)
        .add("lastModifiedBy", lastModifiedBy)
        .toString();
  }
}
