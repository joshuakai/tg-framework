package com.tg.framework.beans.http;

public final class SimpleRequestEntityBuilder extends
    SimpleHttpEntityBuilder<SimpleRequestEntityBuilder> {

  private String method;
  private String url;
  private String contentType;

  private SimpleRequestEntityBuilder() {
  }

  public static SimpleRequestEntityBuilder builder() {
    return new SimpleRequestEntityBuilder();
  }

  public SimpleRequestEntityBuilder method(String method) {
    this.method = method;
    return this;
  }

  public SimpleRequestEntityBuilder url(String url) {
    this.url = url;
    return this;
  }

  public SimpleRequestEntityBuilder contentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  public SimpleRequestEntity build() {
    SimpleRequestEntity simpleRequestEntity = build(new SimpleRequestEntity());
    simpleRequestEntity.setMethod(method);
    simpleRequestEntity.setUrl(url);
    simpleRequestEntity.setContentType(contentType);
    return simpleRequestEntity;
  }
}
