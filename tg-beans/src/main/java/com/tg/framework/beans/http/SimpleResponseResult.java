package com.tg.framework.beans.http;

import java.io.Serializable;

public class SimpleResponseResult implements Serializable {

  private static final long serialVersionUID = -9151088230970759424L;

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
}
