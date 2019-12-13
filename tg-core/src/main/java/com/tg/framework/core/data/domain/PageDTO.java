package com.tg.framework.core.data.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageDTO<T> implements Serializable {

  private static final long serialVersionUID = 115333065420271847L;

  private int number;
  private List<T> content;
  private long totalElements;

  public PageDTO() {
    this(0, new ArrayList<>(0), 0);
  }

  public PageDTO(int number, List<T> content, long totalElements) {
    this.number = number >= 0 ? number : 0;
    this.content = content != null ? content : new ArrayList<>(0);
    this.totalElements = totalElements >= 0 ? totalElements : 0;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(long totalElements) {
    this.totalElements = totalElements;
  }
}
