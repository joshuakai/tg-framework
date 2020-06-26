package com.tg.framework.commons;

import static com.tg.framework.commons.BuildInBusinessErrors.GENERAL_BUSINESS_ERROR;

public class BusinessException extends NestedRuntimeException {

  private static final long serialVersionUID = 1033666761237608767L;

  private String code;
  private Object[] args;
  private boolean i18n;
  private String i18nCode;
  private Object[] i18nArgs;

  public BusinessException(String code, String message) {
    super(message);
    this.code = code;
  }

  public BusinessException(String message) {
    this(GENERAL_BUSINESS_ERROR, message);
  }

  public <T extends BusinessException> T cast(Class<T> clazz) {
    //noinspection unchecked
    return (T) this;
  }

  public BusinessException withArgs(Object... args) {
    this.args = args;
    return this;
  }

  public BusinessException i18n(String i18nCode, Object... i18nArgs) {
    this.i18n = true;
    this.i18nCode = i18nCode;
    this.i18nArgs = i18nArgs;
    return this;
  }

  public String getCode() {
    return code;
  }

  public Object[] getArgs() {
    return args;
  }

  public boolean isI18n() {
    return i18n;
  }

  public String getI18nCode() {
    return i18nCode;
  }

  public Object[] getI18nArgs() {
    return i18nArgs;
  }
}
