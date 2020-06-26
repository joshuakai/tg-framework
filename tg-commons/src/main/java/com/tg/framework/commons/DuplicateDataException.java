package com.tg.framework.commons;

import static com.tg.framework.commons.BuildInBusinessErrors.DATA_DUPLICATE;

public class DuplicateDataException extends DataStatusException {

  private static final long serialVersionUID = -1561576642812485300L;

  public DuplicateDataException(String code, String message) {
    super(code, message);
  }

  public DuplicateDataException(String message) {
    this(DATA_DUPLICATE, message);
  }
}
