package com.tg.framework.web.upload.support;

import com.tg.framework.commons.exception.DomainObjectRequiredException;
import com.tg.framework.commons.util.OptionalUtils;
import com.tg.framework.web.upload.FileUploadService;
import com.tg.framework.web.upload.FilenameResolver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

public class DefaultFileUploadService implements FileUploadService {

  private static Logger logger = LoggerFactory.getLogger(DefaultFileUploadService.class);

  private FileUploadSettings settings;

  private FilenameResolver filenameResolver = new JavaTimeGroupFilenameResolver(
      new RandomMD5FilenameResolver());

  public DefaultFileUploadService(FileUploadSettings settings) {
    Assert.notNull(settings, "FileUploadSettings must not be null.");
    Assert.notEmpty(settings.getAcceptMimeTypes(),
        "FileUploadSettings.acceptMimeTypes must not be empty.");
    Assert.isTrue(StringUtils.isNotBlank(settings.getLocalDir()),
        "FileUploadSettings.localDir must not be empty.");
    File localDir = new File(settings.getLocalDir());
    if (localDir.exists()) {
      Path path = Paths.get(localDir.getAbsolutePath());
      Assert.isTrue(Files.isDirectory(path), "FileUploadSettings.localDir must be a directory.");
      Assert.isTrue(Files.isWritable(path), "FileUploadSettings.localDir must be writable.");
    } else {
      Assert.isTrue(localDir.mkdir(), "FileUploadSettings.localDir can't be created.");
    }
    this.settings = settings;
  }

  public DefaultFileUploadService(FileUploadSettings settings, FilenameResolver filenameResolver) {
    this(settings);
    Assert.notNull(filenameResolver, "FilenameResolver must not be null.");
    this.filenameResolver = filenameResolver;
  }

  @Override
  public String[] store(MultipartFile[] multipartFiles, String requestIp) {
    OptionalUtils.notEmpty(multipartFiles).orElseThrow(DomainObjectRequiredException::new);
    return Stream.of(multipartFiles).map(multipartFile -> store(multipartFile, requestIp))
        .toArray(String[]::new);
  }

  @Override
  public String store(MultipartFile multipartFile, String requestIp) {
    Optional.ofNullable(multipartFile).orElseThrow(DomainObjectRequiredException::new);
    String originalFilename = multipartFile.getOriginalFilename();
    String filename = StringUtils
        .substringAfterLast(originalFilename, FilenameResolver.MIME_TYPE_SEPARATOR);
    String mimeType = StringUtils
        .substringAfterLast(originalFilename, FilenameResolver.MIME_TYPE_SEPARATOR);
    if (!isMimeTypeAccept(mimeType)) {
      logger.warn("Invalid mime type found {} {}.", originalFilename, requestIp);
      throw new UploadException("Bad mime type.", originalFilename);
    }
    if (!isFileNameAccept(filename)) {
      logger.warn("Invalid file name found {} {}.", originalFilename, requestIp);
      throw new UploadException("Bad file name.", originalFilename);
    }
    String finalFilename = filenameResolver.resolve(originalFilename, filename, mimeType);
    File file = new File(
        StringUtils.join(settings.getLocalDir(), finalFilename, FilenameResolver.DIR_SEPARATOR));
    if (file.exists() && !settings.isReplaceExists()) {
      throw new UploadException("File exists.", originalFilename);
    }
    try {
      multipartFile.transferTo(file);
    } catch (IOException e) {
      throw new UploadException("Failed to write file.", e, originalFilename);
    }
    return StringUtils
        .join(settings.getWebBasePath(), finalFilename, FilenameResolver.DIR_SEPARATOR);
  }

  private boolean isMimeTypeAccept(String mimeType) {
    return OptionalUtils.notEmpty(mimeType).map(mt -> settings.getAcceptMimeTypes().stream()
        .anyMatch(acceptMimeType -> StringUtils.equalsIgnoreCase(acceptMimeType, mt)))
        .orElse(false);
  }

  private boolean isFileNameAccept(String filename) {
    return StringUtils.isNotBlank(filename) && OptionalUtils.notEmpty(settings.getFilenamePattern())
        .map(pattern -> Pattern.matches(pattern, filename)).orElse(true);
  }
}
