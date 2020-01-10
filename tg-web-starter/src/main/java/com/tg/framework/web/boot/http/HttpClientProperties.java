package com.tg.framework.web.boot.http;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tg.web.http-client")
public class HttpClientProperties {

  private int maxTotal = 200;
  private int defaultMaxPerRoute = 50;
  private int validateAfterInactivity = 5 * 1000;
  private int connectionRequestTimeout = 1000;
  private int connectTimeout = 10 * 1000;
  private int socketTimeout = 60 * 1000;
  private int maxRetryCount = 3;
  private String disableRedirectionHeaderName = "x-disabled-redirect";
  private String defaultUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36";
  private long defaultKeepAliveDuration = 60 * 1000L;


  public int getMaxTotal() {
    return maxTotal;
  }

  public void setMaxTotal(int maxTotal) {
    this.maxTotal = maxTotal;
  }

  public int getDefaultMaxPerRoute() {
    return defaultMaxPerRoute;
  }

  public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
    this.defaultMaxPerRoute = defaultMaxPerRoute;
  }

  public int getValidateAfterInactivity() {
    return validateAfterInactivity;
  }

  public void setValidateAfterInactivity(int validateAfterInactivity) {
    this.validateAfterInactivity = validateAfterInactivity;
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
