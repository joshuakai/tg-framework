package com.tg.framework.web.upload.support;

import com.tg.framework.core.exception.NestedRuntimeException;

public class UploadException extends NestedRuntimeException {

  public static final String PRESENT_CODE = "Upload";

  public UploadException(String fileName) {
    super(PRESENT_CODE, new Object[]{fileName});
  }

  public UploadException(String fileName, String message) {
    super(PRESENT_CODE, new Object[]{fileName}, message);
  }

  public UploadException(String fileName, String message, Throwable cause) {
    super(PRESENT_CODE, new Object[]{fileName}, message, cause);
  }

  public UploadException(String fileName, Throwable cause) {
    super(PRESENT_CODE, new Object[]{fileName}, cause);
  }

  public UploadException(String fileName, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(PRESENT_CODE, new Object[]{fileName}, message, cause, enableSuppression,
        writableStackTrace);
  }

}
