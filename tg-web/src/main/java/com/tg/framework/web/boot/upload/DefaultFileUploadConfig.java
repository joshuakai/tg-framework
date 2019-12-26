package com.tg.framework.web.boot.upload;

import com.tg.framework.web.boot.mvc.resolver.annotation.RequestIp;
import com.tg.framework.web.upload.FileUploadService;
import com.tg.framework.web.upload.support.DefaultFileUploadService;
import com.tg.framework.web.upload.support.FileUploadSettings;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * TODO File Synchronize
 */
@Deprecated
@Configuration
@ConfigurationProperties("tg.upload.file")
public class DefaultFileUploadConfig {

  private Set<String> acceptMimeTypes = Stream.of("TXT", "XLS", "XLSX").collect(Collectors.toSet());
  private String localDir;
  private String filenamePattern;
  private boolean replaceExists;
  private String webBasePath;

  @Bean("fileUploadConfig")
  public FileUploadSettings fileUploadConfig() {
    return new FileUploadSettings(acceptMimeTypes, localDir, filenamePattern, replaceExists,
        webBasePath);
  }

  @Bean("fileUploadService")
  public FileUploadService fileUploadService() {
    return new DefaultFileUploadService(fileUploadConfig());
  }

  @RestController
  static class FileUploadController {

    @Resource(name = "fileUploadService")
    private FileUploadService fileUploadService;


    @PostMapping("/upload-file")
    public String upload(@RequestParam("file") MultipartFile file, @RequestIp String requestIp) {
      return fileUploadService.store(file, requestIp);
    }

    @PostMapping("/upload-files")
    public String[] upload(@RequestParam("files") MultipartFile[] files,
        @RequestIp String requestIp) {
      return fileUploadService.store(files, requestIp);
    }
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
