package com.tg.framework.web.http;

public class HttpClientConnectionManagerProperties {

  private int maxTotal;
  private int defaultMaxPerRoute;
  private int validateAfterInactivity;

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
}
