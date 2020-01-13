package com.tg.framework.beans.http;

import java.io.Serializable;

public abstract class SimpleHttpEntity implements Serializable {

  private static final long serialVersionUID = -7976283146776014262L;

  protected MultiValueMap<String, String> headers;
  protected String body;

  public MultiValueMap<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(
      MultiValueMap<String, String> headers) {
    this.headers = headers;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
