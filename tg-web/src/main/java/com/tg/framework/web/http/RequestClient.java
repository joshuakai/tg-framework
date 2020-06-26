package com.tg.framework.web.http;

import eu.bitwalker.useragentutils.UserAgent;
import java.io.Serializable;

public class RequestClient implements Serializable {

  private static final long serialVersionUID = 7828951282259341879L;

  private String url;
  private String requestIp;
  private String remoteAddress;
  private String xRealIp;
  private String xForwardedFor;
  private UserAgent userAgent;

  public RequestClient() {
  }

  public RequestClient(String url, String requestIp, String remoteAddress, String xRealIp,
      String xForwardedFor, UserAgent userAgent) {
    this.url = url;
    this.requestIp = requestIp;
    this.remoteAddress = remoteAddress;
    this.xRealIp = xRealIp;
    this.xForwardedFor = xForwardedFor;
    this.userAgent = userAgent;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getRequestIp() {
    return requestIp;
  }

  public void setRequestIp(String requestIp) {
    this.requestIp = requestIp;
  }

  public String getRemoteAddress() {
    return remoteAddress;
  }

  public void setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
  }

  public String getxRealIp() {
    return xRealIp;
  }

  public void setxRealIp(String xRealIp) {
    this.xRealIp = xRealIp;
  }

  public String getxForwardedFor() {
    return xForwardedFor;
  }

  public void setxForwardedFor(String xForwardedFor) {
    this.xForwardedFor = xForwardedFor;
  }

  public UserAgent getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(UserAgent userAgent) {
    this.userAgent = userAgent;
  }

  @Override
  public String toString() {
    return "RequestClient{" +
        "url='" + url + '\'' +
        ", requestIp='" + requestIp + '\'' +
        ", remoteAddress='" + remoteAddress + '\'' +
        ", xRealIp='" + xRealIp + '\'' +
        ", xForwardedFor='" + xForwardedFor + '\'' +
        ", userAgent=" + userAgent +
        '}';
  }
}
