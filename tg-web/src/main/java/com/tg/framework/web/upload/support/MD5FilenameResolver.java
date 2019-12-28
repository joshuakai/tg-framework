package com.tg.framework.web.upload.support;

import com.tg.framework.commons.util.CryptoUtils;
import com.tg.framework.web.upload.FilenameResolver;
import org.apache.commons.lang3.StringUtils;

public class MD5FilenameResolver implements FilenameResolver {

  @Override
  public String resolve(String originalFilename, String filename, String mimeType) {
    return StringUtils.join(CryptoUtils.encryptByMD5(filename), mimeType, MIME_TYPE_SEPARATOR);
  }

}
