package com.tg.framework.web.upload.support;

import com.tg.framework.commons.util.MD5Utils;
import com.tg.framework.web.upload.FilenameResolver;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class RandomMD5FilenameResolver implements FilenameResolver {

  @Override
  public String resolve(String originalFilename, String filename, String mimeType) {
    return StringUtils
        .join(MD5Utils.md5Hex(StringUtils.join(filename, UUID.randomUUID().toString())), mimeType,
            MIME_TYPE_SEPARATOR);
  }
}
