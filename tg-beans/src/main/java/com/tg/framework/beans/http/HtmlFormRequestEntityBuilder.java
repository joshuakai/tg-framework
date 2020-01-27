package com.tg.framework.beans.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class HtmlFormRequestEntityBuilder {

  private static final String GET = "GET";
  private static final String POST = "POST";

  private String url;
  private String method;
  private List<NameValuePair> parameters;

  private HtmlFormRequestEntityBuilder() {
  }

  private List<NameValuePair> requireParameters() {
    if (parameters == null) {
      synchronized (this) {
        if (parameters == null) {
          parameters = new ArrayList<>();
        }
      }
    }
    return parameters;
  }

  public static HtmlFormRequestEntityBuilder builder() {
    return new HtmlFormRequestEntityBuilder();
  }

  public HtmlFormRequestEntityBuilder get(String url) {
    this.url = url;
    this.method = GET;
    return this;
  }

  public HtmlFormRequestEntityBuilder post(String url) {
    this.url = url;
    this.method = POST;
    return this;
  }

  public HtmlFormRequestEntityBuilder addParameter(String name, String value) {
    return addParameter(new NameValuePair(name, value));
  }

  public HtmlFormRequestEntityBuilder addParameter(NameValuePair parameter) {
    requireParameters().add(parameter);
    return this;
  }

  public HtmlFormRequestEntityBuilder addParameter(Map<String, String> parameters) {
    Optional.ofNullable(parameters).ifPresent(params -> params.forEach(this::addParameter));
    return this;
  }

  public HtmlFormRequestEntityBuilder parameters(List<NameValuePair> parameters) {
    this.parameters = parameters;
    return this;
  }

  public HtmlFormRequestEntity build() {
    HtmlFormRequestEntity htmlFormRequestEntity = new HtmlFormRequestEntity();
    htmlFormRequestEntity.setUrl(url);
    htmlFormRequestEntity.setMethod(method);
    htmlFormRequestEntity.setParameters(parameters);
    return htmlFormRequestEntity;
  }
}
