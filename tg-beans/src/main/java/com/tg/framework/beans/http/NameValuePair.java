package com.tg.framework.beans.http;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class NameValuePair implements Serializable {

  private static final long serialVersionUID = 3821538825409856504L;

  @NotBlank
  @Length(max = 100)
  private String name;
  @Length(max = 255)
  private String value;

  public NameValuePair() {
  }

  public NameValuePair(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
