package com.tg.framework.beans.http;

import java.io.Serializable;
import java.util.List;

public class HtmlFormRequestEntity implements Serializable {

  private static final long serialVersionUID = -4833824418674355975L;

  private String url;
  private String method;
  private List<NameValuePair> parameters;

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

  public List<NameValuePair> getParameters() {
    return parameters;
  }

  public void setParameters(List<NameValuePair> parameters) {
    this.parameters = parameters;
  }

}
