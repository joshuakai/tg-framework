package com.tg.framework.beans.http;

public class SimpleResponseEntity extends SimpleHttpEntity {

  private static final long serialVersionUID = -8105581712958728428L;

  private int statusCode;

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }


}
