package com.tg.framework.beans.http;

public final class SimpleResponseEntityBuilder extends
    SimpleHttpEntityBuilder<SimpleResponseEntityBuilder> {

  private int statusCode;

  private SimpleResponseEntityBuilder() {
  }

  public static SimpleResponseEntityBuilder builder() {
    return new SimpleResponseEntityBuilder();
  }

  public SimpleResponseEntityBuilder statusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public SimpleResponseEntity build() {
    SimpleResponseEntity simpleResponseEntity = build(new SimpleResponseEntity());
    simpleResponseEntity.setStatusCode(statusCode);
    return simpleResponseEntity;
  }
}
