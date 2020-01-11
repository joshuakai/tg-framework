package com.tg.framework.beans.http;

public class SimpleRequestEntity extends SimpleHttpEntity {

  private static final long serialVersionUID = 6094631046201319184L;

  private String method;
  private String url;
  private String contentType;

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
}
