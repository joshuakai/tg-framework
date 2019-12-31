package com.tg.framework.web.upload.support;

import com.tg.framework.commons.exception.NestedRuntimeException;

public class UploadException extends NestedRuntimeException {

  private String originalFilename;

  public UploadException(String originalFilename) {
    this.originalFilename = originalFilename;
  }

  public UploadException(String message, String originalFilename) {
    super(message);
    this.originalFilename = originalFilename;
  }

  public UploadException(String message, Throwable cause, String originalFilename) {
    super(message, cause);
    this.originalFilename = originalFilename;
  }

  public UploadException(Throwable cause, String originalFilename) {
    super(cause);
    this.originalFilename = originalFilename;
  }

  public UploadException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, String originalFilename) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.originalFilename = originalFilename;
  }

  public String getOriginalFilename() {
    return originalFilename;
  }
}
