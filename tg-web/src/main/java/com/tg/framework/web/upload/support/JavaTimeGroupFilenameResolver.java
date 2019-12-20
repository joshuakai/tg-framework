package com.tg.framework.web.upload.support;

import com.tg.framework.commons.util.JavaTimeUtils;
import com.tg.framework.web.upload.FilenameResolver;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class JavaTimeGroupFilenameResolver implements FilenameResolver {

  private FilenameResolver delegate;
  private DateTimeFormatter formatter = JavaTimeUtils
      .dateTimeFormatter(JavaTimeUtils.PATTERN_Y_M_D_COMPACT);

  public JavaTimeGroupFilenameResolver(FilenameResolver delegate) {
    Assert.notNull(delegate, "Delegated FilenameResolver must not be null.");
    this.delegate = delegate;
  }

  public JavaTimeGroupFilenameResolver(FilenameResolver delegate, DateTimeFormatter formatter) {
    this(delegate);
    Assert.notNull(formatter, "DateTimeFormatter must not be null.");
    this.formatter = formatter;
  }

  public JavaTimeGroupFilenameResolver(FilenameResolver delegate, String pattern) {
    this(delegate, JavaTimeUtils.dateTimeFormatter(pattern));
  }

  @Override
  public String resolve(String originalFilename, String filename, String mimeType) {
    return StringUtils.join(JavaTimeUtils.format(LocalDateTime.now(), formatter),
        delegate.resolve(originalFilename, filename, mimeType), DIR_SEPARATOR);
  }
}
