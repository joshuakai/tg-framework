package com.tg.framework.commons.util;

import com.tg.framework.commons.lang.StringOptional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class JavaTimeUtils {

  public static final String PATTERN_Y_M_D_H_MI_S = "yyyy-MM-dd HH:mm:ss";
  public static final String PATTERN_Y_M_D = "yyyy-MM-dd";
  public static final String PATTERN_H_MI_S = "HH:mm:ss";

  private JavaTimeUtils() {
  }

  public static SimpleDateFormat simpleDateFormat(String pattern, Locale locale, boolean lenient) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
    simpleDateFormat.setLenient(lenient);
    return simpleDateFormat;
  }

  public static SimpleDateFormat simpleDateFormat(String pattern, Locale locale) {
    return simpleDateFormat(pattern, locale, false);
  }

  public static SimpleDateFormat simpleDateFormat(String pattern, boolean lenient) {
    return simpleDateFormat(pattern, Locale.getDefault(), lenient);
  }

  public static SimpleDateFormat simpleDateFormat(String pattern) {
    return simpleDateFormat(pattern, Locale.getDefault());
  }

  public static SimpleDateFormat dateTimeFormat(Locale locale, boolean lenient) {
    return simpleDateFormat(PATTERN_Y_M_D_H_MI_S, locale, lenient);
  }

  public static SimpleDateFormat dateTimeFormat(Locale locale) {
    return dateTimeFormat(locale, false);
  }

  public static SimpleDateFormat dateTimeFormat(boolean lenient) {
    return dateTimeFormat(Locale.getDefault(), lenient);
  }

  public static SimpleDateFormat dateTimeFormat() {
    return dateTimeFormat(Locale.getDefault());
  }

  public static SimpleDateFormat dateFormat(Locale locale, boolean lenient) {
    return simpleDateFormat(PATTERN_Y_M_D, locale, lenient);
  }

  public static SimpleDateFormat dateFormat(Locale locale) {
    return dateFormat(locale, false);
  }

  public static SimpleDateFormat dateFormat(boolean lenient) {
    return dateFormat(Locale.getDefault(), lenient);
  }

  public static SimpleDateFormat dateFormat() {
    return dateFormat(Locale.getDefault());
  }

  public static SimpleDateFormat timeFormat(Locale locale, boolean lenient) {
    return simpleDateFormat(PATTERN_H_MI_S, locale, lenient);
  }

  public static SimpleDateFormat timeFormat(Locale locale) {
    return timeFormat(locale, false);
  }

  public static SimpleDateFormat timeFormat(boolean lenient) {
    return timeFormat(Locale.getDefault(), lenient);
  }

  public static SimpleDateFormat timeFormat() {
    return timeFormat(Locale.getDefault());
  }

  public static String format(Date date, DateFormat dateFormat) {
    return Optional.ofNullable(date)
        .map(d -> Optional.ofNullable(dateFormat).map(df -> df.format(d)).orElse(null))
        .orElse(null);
  }

  public static String format(Date date, String pattern, Locale locale, boolean lenient) {
    return format(date, simpleDateFormat(pattern, locale, lenient));
  }

  public static String format(Date date, String pattern, Locale locale) {
    return format(date, simpleDateFormat(pattern, locale));
  }

  public static String format(Date date, String pattern, boolean lenient) {
    return format(date, simpleDateFormat(pattern, lenient));
  }

  public static String format(Date date, String pattern) {
    return format(date, simpleDateFormat(pattern));
  }

  public static String formatDateTime(Date date, Locale locale, boolean lenient) {
    return format(date, dateTimeFormat(locale, lenient));
  }

  public static String formatDateTime(Date date, Locale locale) {
    return format(date, dateTimeFormat(locale));
  }

  public static String formatDateTime(Date date, boolean lenient) {
    return format(date, dateTimeFormat(lenient));
  }

  public static String formatDateTime(Date date) {
    return format(date, dateTimeFormat());
  }

  public static String formatDate(Date date, Locale locale, boolean lenient) {
    return format(date, dateFormat(locale, lenient));
  }

  public static String formatDate(Date date, Locale locale) {
    return format(date, dateFormat(locale));
  }

  public static String formatDate(Date date, boolean lenient) {
    return format(date, dateFormat(lenient));
  }

  public static String formatDate(Date date) {
    return format(date, dateFormat());
  }

  public static String formatTime(Date date, Locale locale, boolean lenient) {
    return format(date, timeFormat(locale, lenient));
  }

  public static String formatTime(Date date, Locale locale) {
    return format(date, timeFormat(locale));
  }

  public static String formatTime(Date date, boolean lenient) {
    return format(date, timeFormat(lenient));
  }

  public static String formatTime(Date date) {
    return format(date, timeFormat());
  }

  public static Date parse(String str, DateFormat dateFormat, boolean throwException) {
    return StringOptional.ofNullable(str).map(s -> Optional.ofNullable(dateFormat).map(df -> {
      try {
        return df.parse(s);
      } catch (ParseException e) {
        if (throwException) {
          throw new IllegalArgumentException(e);
        }
        return null;
      }
    }).orElse(null)).orElse(null);
  }

  public static Date parse(String str, DateFormat dateFormat) {
    return parse(str, dateFormat, true);
  }

  public static Date parse(String str, String pattern, Locale locale, boolean lenient) {
    return parse(str, simpleDateFormat(pattern, locale, lenient));
  }

  public static Date parse(String str, String pattern, Locale locale) {
    return parse(str, simpleDateFormat(pattern, locale));
  }

  public static Date parse(String str, String pattern, boolean lenient) {
    return parse(str, simpleDateFormat(pattern, lenient));
  }

  public static Date parse(String str, String pattern) {
    return parse(str, simpleDateFormat(pattern));
  }

  public static Date parseDateTime(String str, Locale locale, boolean lenient) {
    return parse(str, dateTimeFormat(locale, lenient));
  }

  public static Date parseDateTime(String str, Locale locale) {
    return parse(str, dateTimeFormat(locale));
  }

  public static Date parseDateTime(String str, boolean lenient) {
    return parse(str, dateTimeFormat(lenient));
  }

  public static Date parseDateTime(String str) {
    return parse(str, dateTimeFormat());
  }

  public static Date parseDate(String str, Locale locale, boolean lenient) {
    return parse(str, dateFormat(locale, lenient));
  }

  public static Date parseDate(String str, Locale locale) {
    return parse(str, dateFormat(locale));
  }

  public static Date parseDate(String str, boolean lenient) {
    return parse(str, dateFormat(lenient));
  }

  public static Date parseDate(String str) {
    return parse(str, dateFormat());
  }

  public static Date parseTime(String str, Locale locale, boolean lenient) {
    return parse(str, timeFormat(locale, lenient));
  }

  public static Date parseTime(String str, Locale locale) {
    return parse(str, timeFormat(locale));
  }

  public static Date parseTime(String str, boolean lenient) {
    return parse(str, timeFormat(lenient));
  }

  public static Date parseTime(String str) {
    return parse(str, timeFormat());
  }

  public static DateTimeFormatter dateTimeFormatter(String pattern, Locale locale) {
    return DateTimeFormatter.ofPattern(pattern, locale);
  }

  public static DateTimeFormatter dateTimeFormatter(String pattern) {
    return dateTimeFormatter(pattern, Locale.getDefault());
  }

  public static DateTimeFormatter localDateTimeFormatter(Locale locale) {
    return dateTimeFormatter(PATTERN_Y_M_D_H_MI_S, locale);
  }

  public static DateTimeFormatter localDateTimeFormatter() {
    return localDateTimeFormatter(Locale.getDefault());
  }

  public static DateTimeFormatter localDateFormatter(Locale locale) {
    return dateTimeFormatter(PATTERN_Y_M_D, locale);
  }

  public static DateTimeFormatter localDateFormatter() {
    return localDateFormatter(Locale.getDefault());
  }

  public static DateTimeFormatter localTimeFormatter(Locale locale) {
    return dateTimeFormatter(PATTERN_H_MI_S, locale);
  }

  public static DateTimeFormatter localTimeFormatter() {
    return localTimeFormatter(Locale.getDefault());
  }

  public static String format(TemporalAccessor temporalAccessor,
      DateTimeFormatter dateTimeFormatter) {
    return Optional.ofNullable(temporalAccessor)
        .map(t -> Optional.ofNullable(dateTimeFormatter).map(df -> df.format(t)).orElse(null))
        .orElse(null);
  }

  public static String format(TemporalAccessor temporalAccessor, String pattern, Locale locale) {
    return format(temporalAccessor, dateTimeFormatter(pattern, locale));
  }

  public static String format(TemporalAccessor temporalAccessor, String pattern) {
    return format(temporalAccessor, dateTimeFormatter(pattern));
  }

  public static String format(LocalDateTime localDateTime, Locale locale) {
    return format(localDateTime, localDateTimeFormatter(locale));
  }

  public static String format(LocalDateTime localDateTime) {
    return format(localDateTime, Locale.getDefault());
  }

  public static String format(LocalDate localDate, Locale locale) {
    return format(localDate, localDateFormatter(locale));
  }

  public static String format(LocalDate localDate) {
    return format(localDate, Locale.getDefault());
  }

  public static String format(LocalTime localTime, Locale locale) {
    return format(localTime, localTimeFormatter(locale));
  }

  public static String format(LocalTime localTime) {
    return format(localTime, Locale.getDefault());
  }

  public static LocalDateTime parseLocalDateTime(String str, DateTimeFormatter dateTimeFormatter) {
    return StringOptional.ofNullable(str).map(
        s -> Optional.ofNullable(dateTimeFormatter).map(df -> LocalDateTime.parse(str, df))
            .orElse(null)).orElse(null);
  }

  public static LocalDateTime parseLocalDateTime(String str, String pattern, Locale locale) {
    return parseLocalDateTime(str, dateTimeFormatter(pattern, locale));
  }

  public static LocalDateTime parseLocalDateTime(String str, String pattern) {
    return parseLocalDateTime(str, dateTimeFormatter(pattern));
  }

  public static LocalDateTime parseLocalDateTime(String str, Locale locale) {
    return parseLocalDateTime(str, localDateTimeFormatter(locale));
  }

  public static LocalDateTime parseLocalDateTime(String str) {
    return parseLocalDateTime(str, localDateTimeFormatter());
  }


  public static LocalDate parseLocalDate(String str, DateTimeFormatter dateTimeFormatter) {
    return StringOptional.ofNullable(str).map(
        s -> Optional.ofNullable(dateTimeFormatter).map(df -> LocalDate.parse(str, df))
            .orElse(null)).orElse(null);
  }

  public static LocalDate parseLocalDate(String str, String pattern, Locale locale) {
    return parseLocalDate(str, dateTimeFormatter(pattern, locale));
  }

  public static LocalDate parseLocalDate(String str, String pattern) {
    return parseLocalDate(str, dateTimeFormatter(pattern));
  }

  public static LocalDate parseLocalDate(String str, Locale locale) {
    return parseLocalDate(str, localDateFormatter(locale));
  }

  public static LocalDate parseLocalDate(String str) {
    return parseLocalDate(str, localDateFormatter());
  }


  public static LocalTime parseLocalTime(String str, DateTimeFormatter dateTimeFormatter) {
    return StringOptional.ofNullable(str).map(
        s -> Optional.ofNullable(dateTimeFormatter).map(df -> LocalTime.parse(str, df))
            .orElse(null)).orElse(null);
  }

  public static LocalTime parseLocalTime(String str, String pattern, Locale locale) {
    return parseLocalTime(str, dateTimeFormatter(pattern, locale));
  }

  public static LocalTime parseLocalTime(String str, String pattern) {
    return parseLocalTime(str, dateTimeFormatter(pattern));
  }

  public static LocalTime parseLocalTime(String str, Locale locale) {
    return parseLocalTime(str, localTimeFormatter(locale));
  }

  public static LocalTime parseLocalTime(String str) {
    return parseLocalTime(str, localTimeFormatter());
  }

}
