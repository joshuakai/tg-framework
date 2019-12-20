package com.tg.framework.beans.http;

import java.io.Serializable;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class ProxyRequestBean implements Serializable {

  private static final long serialVersionUID = 6892306500233740802L;

  @NotBlank
  private String url;
  @NotBlank
  @Length(max = 10)
  private String method;
  private boolean usingPayload;
  private Map<String, Object> parameters;
  private Object payload;
  private Map<String, Object> uriVariables;

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

  public boolean isUsingPayload() {
    return usingPayload;
  }

  public void setUsingPayload(boolean usingPayload) {
    this.usingPayload = usingPayload;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  public Map<String, Object> getUriVariables() {
    return uriVariables;
  }

  public void setUriVariables(Map<String, Object> uriVariables) {
    this.uriVariables = uriVariables;
  }
}
