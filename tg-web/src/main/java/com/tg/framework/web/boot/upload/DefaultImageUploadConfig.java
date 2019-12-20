package com.tg.framework.web.boot.upload;

import com.tg.framework.commons.http.RequestDetails;
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
@ConfigurationProperties("tg.upload.image")
public class DefaultImageUploadConfig {

  private Set<String> acceptMimeTypes = Stream.of("BPM", "ICO", "JPG", "JEPG", "PNG", "GIF", "SWF").collect(Collectors.toSet());
  private String localDir;
  private String filenamePattern;
  private boolean replaceExists;
  private String webBasePath;

  @Bean("imageUploadConfig")
  public FileUploadSettings imageUploadConfig() {
    return new FileUploadSettings(acceptMimeTypes, localDir, filenamePattern, replaceExists,
        webBasePath);
  }

  @Bean("imageUploadService")
  public FileUploadService imageUploadService() {
    return new DefaultFileUploadService(imageUploadConfig());
  }

  @RestController
  static class ImageUploadController {

    @Resource(name = "imageUploadService")
    private FileUploadService imageUploadService;


    @PostMapping("/upload-image")
    public String upload(@RequestParam("image") MultipartFile image, RequestDetails requestDetails)
        throws Exception {
      return imageUploadService.store(image, requestDetails);
    }

    @PostMapping("/upload-images")
    public String[] upload(@RequestParam("images") MultipartFile[] images,
        RequestDetails requestDetails) {
      return imageUploadService.store(images, requestDetails);
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
