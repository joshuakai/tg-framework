package com.tg.framework.web.boot.mvc.formatter;

import static com.tg.framework.commons.util.JavaTimeUtils.PATTERN_H_MI_S;

import com.tg.framework.commons.util.JavaTimeUtils;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;
import org.springframework.format.Formatter;

public class LocalTimeFormatter implements Formatter<LocalTime> {

  private String pattern = PATTERN_H_MI_S;

  public LocalTimeFormatter() {
  }

  public LocalTimeFormatter(String pattern) {
    this.pattern = pattern;
  }

  @Override
  public LocalTime parse(String text, Locale locale) throws ParseException {
    return JavaTimeUtils.parseLocalTime(text, pattern, locale);
  }

  @Override
  public String print(LocalTime object, Locale locale) {
    return JavaTimeUtils.format(object, pattern, locale);
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }
}
