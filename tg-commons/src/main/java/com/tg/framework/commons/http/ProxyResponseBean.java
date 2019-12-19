package com.tg.framework.commons.http;

import java.io.Serializable;

public class ProxyResponseBean implements Serializable {

  private boolean succeed;
  private String message;
  private String body;

  public boolean isSucceed() {
    return succeed;
  }

  public void setSucceed(boolean succeed) {
    this.succeed = succeed;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public static ProxyResponseBean success(String body) {
    ProxyResponseBean proxyResponse = new ProxyResponseBean();
    proxyResponse.setSucceed(true);
    proxyResponse.setBody(body);
    return proxyResponse;
  }

  public static ProxyResponseBean fail(String message) {
    ProxyResponseBean proxyResponse = new ProxyResponseBean();
    proxyResponse.setSucceed(false);
    proxyResponse.setMessage(message);
    return proxyResponse;
  }
}
