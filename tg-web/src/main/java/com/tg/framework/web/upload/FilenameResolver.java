package com.tg.framework.web.upload;

public interface FilenameResolver {

  String DIR_SEPARATOR = "/";
  String MIME_TYPE_SEPARATOR = ".";

  String resolve(String originalFilename, String filename, String mimeType);

}
