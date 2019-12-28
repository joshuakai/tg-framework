package com.tg.framework.web.mvc.formatter;

import static com.tg.framework.commons.util.JavaTimeUtils.PATTERN_Y_M_D;

import com.tg.framework.commons.util.JavaTimeUtils;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;
import org.springframework.format.Formatter;

public class LocalDateFormatter implements Formatter<LocalDate> {

  private String pattern = PATTERN_Y_M_D;

  public LocalDateFormatter() {
  }

  public LocalDateFormatter(String pattern) {
    this.pattern = pattern;
  }

  @Override
  public LocalDate parse(String text, Locale locale) throws ParseException {
    return JavaTimeUtils.parseLocalDate(text, pattern, locale);
  }

  @Override
  public String print(LocalDate object, Locale locale) {
    return JavaTimeUtils.format(object, pattern, locale);
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }
}
