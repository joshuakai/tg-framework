package com.tg.framework.commons;

import static com.tg.framework.commons.BuildInBusinessErrors.PARAM_REQUIRED;

public class ParamRequiredException extends InvalidParamException {

  private static final long serialVersionUID = 5349477166179711885L;

  public ParamRequiredException(String code, String message) {
    super(code, message);
  }

  public ParamRequiredException(String message) {
    this(PARAM_REQUIRED, message);
  }
}
