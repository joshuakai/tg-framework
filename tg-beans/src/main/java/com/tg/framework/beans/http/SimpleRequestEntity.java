package com.tg.framework.beans.http;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class SimpleRequestEntity extends SimpleHttpEntity {

  private static final long serialVersionUID = 6094631046201319184L;

  @NotBlank
  @Length(max = 10)
  private String method;
  @NotBlank
  @Length(max = 255)
  private String url;
  @Length(max = 50)
  private String contentType;
  @Length(max = 20)
  private String charset;

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

  public String getCharset() {
    return charset;
  }

  public void setCharset(String charset) {
    this.charset = charset;
  }
}
