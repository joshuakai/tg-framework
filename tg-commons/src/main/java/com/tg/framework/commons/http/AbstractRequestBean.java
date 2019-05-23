package com.tg.framework.commons.http;

import java.io.Serializable;
import java.util.Map;

public abstract class AbstractRequestBean implements Serializable {

  private static final long serialVersionUID = -3722387247463094762L;

  protected String url;
  protected String method;
  protected String charset;
  protected Map<String, String> headers;
  protected boolean usingPayload;
  protected Map<String, String> parameters;
  protected String payload;

  public AbstractRequestBean() {
  }

  public AbstractRequestBean(String url, String method, String charset,
      Map<String, String> headers, boolean usingPayload,
      Map<String, String> parameters, String payload) {
    this.url = url;
    this.method = method;
    this.charset = charset;
    this.headers = headers;
    this.usingPayload = usingPayload;
    this.parameters = parameters;
    this.payload = payload;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getCharset() {
    return charset;
  }

  public void setCharset(String charset) {
    this.charset = charset;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public boolean isUsingPayload() {
    return usingPayload;
  }

  public void setUsingPayload(boolean usingPayload) {
    this.usingPayload = usingPayload;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

}
