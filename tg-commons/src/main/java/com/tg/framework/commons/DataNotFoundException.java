package com.tg.framework.commons;

import static com.tg.framework.commons.BuildInBusinessErrors.DATA_NOT_FOUND;

public class DataNotFoundException extends DataStatusException {

  private static final long serialVersionUID = -123804286710666491L;

  public DataNotFoundException(String code, String message) {
    super(code, message);
  }

  public DataNotFoundException(String message) {
    this(DATA_NOT_FOUND, message);
  }

}
