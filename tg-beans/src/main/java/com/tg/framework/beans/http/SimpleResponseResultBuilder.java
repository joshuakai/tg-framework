package com.tg.framework.beans.http;

public final class SimpleResponseResultBuilder {

  private boolean succeed;
  private String message;
  private String body;

  private SimpleResponseResultBuilder() {
  }

  public static SimpleResponseResultBuilder builder() {
    return new SimpleResponseResultBuilder();
  }

  public static SimpleResponseResult succeed(String body) {
    return new SimpleResponseResultBuilder().succeed(true).body(body).build();
  }

  public static SimpleResponseResult failed(String message) {
    return new SimpleResponseResultBuilder().message(message).build();
  }

  public SimpleResponseResultBuilder succeed(boolean succeed) {
    this.succeed = succeed;
    return this;
  }

  public SimpleResponseResultBuilder message(String message) {
    this.message = message;
    return this;
  }

  public SimpleResponseResultBuilder body(String body) {
    this.body = body;
    return this;
  }

  public SimpleResponseResult build() {
    SimpleResponseResult simpleResponseResult = new SimpleResponseResult();
    simpleResponseResult.setSucceed(succeed);
    simpleResponseResult.setMessage(message);
    simpleResponseResult.setBody(body);
    return simpleResponseResult;
  }
}
