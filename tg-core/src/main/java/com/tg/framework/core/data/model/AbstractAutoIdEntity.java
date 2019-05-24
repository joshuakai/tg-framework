package com.tg.framework.core.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.springframework.data.domain.Persistable;

@MappedSuperclass
public abstract class AbstractAutoIdEntity<ID extends Serializable> implements Persistable<ID>,
    Serializable {

  private static final long serialVersionUID = 4923000479829730719L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected ID id;

  @JsonIgnore
  protected transient boolean persisted = true;

  @Override
  @JsonIgnore
  public boolean isNew() {
    return !persisted;
  }

  public void setPersisted(boolean persisted) {
    this.persisted = persisted;
  }

  @Override
  public ID getId() {
    return this.id;
  }

  public void setId(ID id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractAutoIdEntity that = (AbstractAutoIdEntity) o;
    return com.google.common.base.Objects.equal(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .toString();
  }
}
