package com.tg.framework.beans.http;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class SimpleHttpEntityBuilder<T extends SimpleHttpEntityBuilder> {

  protected MultiValueMap<String, String> headers;
  protected String body;

  public T headers(MultiValueMap<String, String> headers) {
    this.headers = headers;
    return (T) this;
  }


  public T addHeader(String key, String value) {
    return (T) this;
  }

  public T addHeader(String key, List<String> values) {
    return (T) this;
  }

  public T addHeader(MultiValueMap<String, String> values) {
    return (T) this;
  }

  public T setHeader(String key, String value) {
    return (T) this;
  }

  public T setHeader(Map<String, String> values) {
    return (T) this;
  }

  public T body(String body) {
    this.body = body;
    return (T) this;
  }

  protected <E extends SimpleHttpEntity> E build(E entity) {
    entity.setHeaders(headers);
    entity.setBody(body);
    return entity;
  }

}
