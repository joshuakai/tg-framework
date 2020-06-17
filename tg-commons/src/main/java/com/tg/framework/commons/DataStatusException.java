package com.tg.framework.commons;

import static com.tg.framework.commons.BuildInBusinessErrors.DATA_STATUS;

public class DataStatusException extends BusinessException {

  private static final long serialVersionUID = -6855120704772347164L;

  public DataStatusException(String code, String message) {
    super(code, message);
  }

  public DataStatusException(String message) {
    this(DATA_STATUS, message);
  }

}
