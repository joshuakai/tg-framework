package com.tg.framework.commons.http;

import java.util.Map;

public final class SendRequestBean extends AbstractRequestBean {

  private static final long serialVersionUID = -5004254869560993910L;

  public SendRequestBean() {
  }

  public SendRequestBean(String url, String method, String charset,
      Map<String, String> headers, boolean usingPayload,
      Map<String, String> parameters, String payload) {
    super(url, method, charset, headers, usingPayload, parameters, payload);
  }
}
