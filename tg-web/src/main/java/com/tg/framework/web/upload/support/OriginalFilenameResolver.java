package com.tg.framework.web.upload.support;

import com.tg.framework.web.upload.FilenameResolver;

public class OriginalFilenameResolver implements FilenameResolver {

  @Override
  public final String resolve(String originalFilename, String filename, String mimeType) {
    return originalFilename;
  }
}
