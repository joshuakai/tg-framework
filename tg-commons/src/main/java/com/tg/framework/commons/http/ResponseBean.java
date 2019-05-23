package com.tg.framework.commons.http;

import java.io.Serializable;
import java.util.Map;

public final class ResponseBean implements Serializable {

  private static final long serialVersionUID = -4657141802616136225L;

  private int status;
  private Map<String, String> headers;
  private String body;

  public ResponseBean() {
  }

  public ResponseBean(int status, Map<String, String> headers, String body) {
    this.status = status;
    this.headers = headers;
    this.body = body;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

}
