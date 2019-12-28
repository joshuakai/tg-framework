package com.tg.framework.web.upload.support;

import java.util.Set;

public class FileUploadSettings {

  private Set<String> acceptMimeTypes;
  private String localDir;
  private String filenamePattern;
  private boolean replaceExists;
  private String webBasePath;

  public FileUploadSettings(Set<String> acceptMimeTypes, String localDir, String filenamePattern,
      boolean replaceExists, String webBasePath) {
    this.acceptMimeTypes = acceptMimeTypes;
    this.localDir = localDir;
    this.filenamePattern = filenamePattern;
    this.replaceExists = replaceExists;
    this.webBasePath = webBasePath;
  }

  public Set<String> getAcceptMimeTypes() {
    return acceptMimeTypes;
  }

  public void setAcceptMimeTypes(Set<String> acceptMimeTypes) {
    this.acceptMimeTypes = acceptMimeTypes;
  }

  public String getLocalDir() {
    return localDir;
  }

  public void setLocalDir(String localDir) {
    this.localDir = localDir;
  }

  public String getFilenamePattern() {
    return filenamePattern;
  }

  public void setFilenamePattern(String filenamePattern) {
    this.filenamePattern = filenamePattern;
  }

  public boolean isReplaceExists() {
    return replaceExists;
  }

  public void setReplaceExists(boolean replaceExists) {
    this.replaceExists = replaceExists;
  }

  public String getWebBasePath() {
    return webBasePath;
  }

  public void setWebBasePath(String webBasePath) {
    this.webBasePath = webBasePath;
  }
}
