package com.tg.framework.web.mvc.formatter;

import static com.tg.framework.commons.util.JavaTimeUtils.PATTERN_Y_M_D_H_MI_S;

import com.tg.framework.commons.util.JavaTimeUtils;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;
import org.springframework.format.Formatter;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

  private String pattern = PATTERN_Y_M_D_H_MI_S;

  public LocalDateTimeFormatter() {
  }

  public LocalDateTimeFormatter(String pattern) {
    this.pattern = pattern;
  }

  @Override
  public LocalDateTime parse(String text, Locale locale) throws ParseException {
    return JavaTimeUtils.parseLocalDateTime(text, pattern, locale);
  }

  @Override
  public String print(LocalDateTime object, Locale locale) {
    return JavaTimeUtils.format(object, pattern, locale);
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }
}
