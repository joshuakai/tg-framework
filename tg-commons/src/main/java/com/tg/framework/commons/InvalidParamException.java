package com.tg.framework.commons;

import static com.tg.framework.commons.BuildInBusinessErrors.PARAM_INVALID;

public class InvalidParamException extends BusinessException {

  private static final long serialVersionUID = -3213522304628755419L;

  public InvalidParamException(String code, String message) {
    super(code, message);
  }

  public InvalidParamException(String message) {
    this(PARAM_INVALID, message);
  }

}
