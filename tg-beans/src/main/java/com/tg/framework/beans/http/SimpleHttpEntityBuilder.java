package com.tg.framework.beans.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public abstract class SimpleHttpEntityBuilder<T extends SimpleHttpEntityBuilder> {

  protected List<NameValuePair> headers;
  protected String body;

  private List<NameValuePair> requireHeaders() {
    if (headers == null) {
      synchronized (this) {
        if (headers == null) {
          headers = new ArrayList<>();
        }
      }
    }
    return headers;
  }

  public T headers(List<NameValuePair> headers) {
    this.headers = headers;
    return (T) this;
  }


  public T addHeader(String name, String value) {
    requireHeaders().add(new NameValuePair(name, value));
    return (T) this;
  }

  public T addHeader(String name, List<String> values) {
    Optional.ofNullable(values).ifPresent(vs -> {
      List<NameValuePair> headers = requireHeaders();
      vs.forEach(v -> headers.add(new NameValuePair(name, v)));
    });
    return (T) this;
  }

  public T addHeader(List<NameValuePair> values) {
    Optional.ofNullable(values).ifPresent(vs -> requireHeaders().addAll(vs));
    return (T) this;
  }

  public T setHeader(String key, String value) {
    requireHeaders().removeIf(p -> p.getName().equals(key));
    requireHeaders().add(new NameValuePair(key, value));
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
