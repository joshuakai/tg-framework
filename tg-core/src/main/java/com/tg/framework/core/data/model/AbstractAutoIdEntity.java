package com.tg.framework.core.data.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractAutoIdEntity<ID extends Serializable> implements Serializable {

  private static final long serialVersionUID = 4923000479829730719L;

  @Id
  protected ID id;

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
