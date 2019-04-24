package com.tg.framework.web.boot.mvc.formatter;

import static com.tg.framework.commons.util.JavaTimeUtils.PATTERN_H_MI_S;
import static com.tg.framework.commons.util.JavaTimeUtils.PATTERN_Y_M_D;
import static com.tg.framework.commons.util.JavaTimeUtils.PATTERN_Y_M_D_H_MI_S;

import com.tg.framework.commons.util.JavaTimeUtils;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.Formatter;

public class CompositeDateFormatter implements Formatter<Date> {

  private String defaultPattern;

  public CompositeDateFormatter() {
  }

  public CompositeDateFormatter(String defaultPattern) {
    this.defaultPattern = defaultPattern;
  }

  @Override
  public Date parse(String text, Locale locale) throws ParseException {
    if (StringUtils.isBlank(text)) {
      return null;
    } else if (text.length() == PATTERN_Y_M_D_H_MI_S.length()) {
      return JavaTimeUtils.parseDateTime(text, locale);
    } else if (text.length() == PATTERN_Y_M_D.length()) {
      return JavaTimeUtils.parseDate(text, locale);
    } else if (text.length() == PATTERN_H_MI_S.length()) {
      return JavaTimeUtils.parseTime(text, locale);
    } else if (defaultPattern != null) {
      return JavaTimeUtils.parse(text, defaultPattern, locale);
    }
    throw new ParseException(text, 0);
  }

  @Override
  public String print(Date object, Locale locale) {
    return JavaTimeUtils.formatDateTime(object, locale);
  }

  public String getDefaultPattern() {
    return defaultPattern;
  }

  public void setDefaultPattern(String defaultPattern) {
    this.defaultPattern = defaultPattern;
  }
}
