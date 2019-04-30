package com.tg.framework.commons.http;

import com.google.common.base.MoreObjects;
import eu.bitwalker.useragentutils.UserAgent;
import java.io.Serializable;

public class RequestDetails implements Serializable {

  private String sessionId;
  private String url;
  private String remoteAddress;
  private String xRealIp;
  private String xForwardedFor;
  private UserAgent userAgent;

  public RequestDetails() {
  }

  public RequestDetails(String sessionId, String url, String remoteAddress, String xRealIp,
      String xForwardedFor, UserAgent userAgent) {
    this.sessionId = sessionId;
    this.url = url;
    this.remoteAddress = remoteAddress;
    this.xRealIp = xRealIp;
    this.xForwardedFor = xForwardedFor;
    this.userAgent = userAgent;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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
    return MoreObjects.toStringHelper(this)
        .add("sessionId", sessionId)
        .add("url", url)
        .add("remoteAddress", remoteAddress)
        .add("xRealIp", xRealIp)
        .add("xForwardedFor", xForwardedFor)
        .add("userAgent", userAgent)
        .toString();
  }
}
