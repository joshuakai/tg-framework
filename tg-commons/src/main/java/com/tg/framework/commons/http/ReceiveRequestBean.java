package com.tg.framework.commons.http;

import java.util.Map;

public final class ReceiveRequestBean extends AbstractRequestBean {

  private static final long serialVersionUID = 3743996362462624112L;

  protected String remoteAddress;
  protected boolean mobileOrTablet;

  public ReceiveRequestBean() {
  }

  public ReceiveRequestBean(String url, String method, String charset,
      Map<String, String> headers, boolean usingPayload,
      Map<String, String> parameters, String payload, String remoteAddress,
      boolean mobileOrTablet) {
    super(url, method, charset, headers, usingPayload, parameters, payload);
    this.remoteAddress = remoteAddress;
    this.mobileOrTablet = mobileOrTablet;
  }

  public String getRemoteAddress() {
    return remoteAddress;
  }

  public void setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
  }

  public boolean isMobileOrTablet() {
    return mobileOrTablet;
  }

  public void setMobileOrTablet(boolean mobileOrTablet) {
    this.mobileOrTablet = mobileOrTablet;
  }

}
