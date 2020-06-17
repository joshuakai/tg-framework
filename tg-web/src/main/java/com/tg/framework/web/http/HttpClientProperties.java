package com.tg.framework.web.http;

public class HttpClientProperties {

  private HttpClientConnectionManagerProperties connectionManagerProperties;
  private int connectionRequestTimeout;
  private int connectTimeout;
  private int socketTimeout;
  private int maxRetryCount;
  private String disableRedirectionHeaderName;
  private String defaultUserAgent;
  private long defaultKeepAliveDuration;

  public HttpClientConnectionManagerProperties getConnectionManagerProperties() {
    return connectionManagerProperties;
  }

  public void setConnectionManagerProperties(
      HttpClientConnectionManagerProperties connectionManagerProperties) {
    this.connectionManagerProperties = connectionManagerProperties;
  }

  public int getConnectionRequestTimeout() {
    return connectionRequestTimeout;
  }

  public void setConnectionRequestTimeout(int connectionRequestTimeout) {
    this.connectionRequestTimeout = connectionRequestTimeout;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public void setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public int getSocketTimeout() {
    return socketTimeout;
  }

  public void setSocketTimeout(int socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  public int getMaxRetryCount() {
    return maxRetryCount;
  }

  public void setMaxRetryCount(int maxRetryCount) {
    this.maxRetryCount = maxRetryCount;
  }

  public String getDisableRedirectionHeaderName() {
    return disableRedirectionHeaderName;
  }

  public void setDisableRedirectionHeaderName(String disableRedirectionHeaderName) {
    this.disableRedirectionHeaderName = disableRedirectionHeaderName;
  }

  public String getDefaultUserAgent() {
    return defaultUserAgent;
  }

  public void setDefaultUserAgent(String defaultUserAgent) {
    this.defaultUserAgent = defaultUserAgent;
  }

  public long getDefaultKeepAliveDuration() {
    return defaultKeepAliveDuration;
  }

  public void setDefaultKeepAliveDuration(long defaultKeepAliveDuration) {
    this.defaultKeepAliveDuration = defaultKeepAliveDuration;
  }
}
