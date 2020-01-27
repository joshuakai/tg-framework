package com.tg.framework.beans.http;

import java.io.Serializable;
import java.util.List;

public class SimpleHttpEntity implements Serializable {

  private static final long serialVersionUID = -7976283146776014262L;

  protected List<NameValuePair> headers;
  protected String body;

  public List<NameValuePair> getHeaders() {
    return headers;
  }

  public void setHeaders(List<NameValuePair> headers) {
    this.headers = headers;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
