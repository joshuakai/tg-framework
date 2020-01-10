package com.tg.framework.commons.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class JavaTimeUtils {

  public static final String PATTERN_Y_M_D = "yyyy-MM-dd";
  public static final String PATTERN_Y_M = "yyyy-MM";
  public static final String PATTERN_Y_W = "YYYY-ww";
  public static final String PATTERN_H_MI_S = "HH:mm:ss";
  public static final String PATTERN_Y_M_D_H_MI_S = PATTERN_Y_M_D + " " + PATTERN_H_MI_S;

  public static final String PATTERN_Y_M_D_COMPACT = "yyyyMMdd";
  public static final String PATTERN_Y_M_COMPACT = "yyyyMM";
  public static final String PATTERN_Y_W_COMPACT = "YYYYww";
  public static final String PATTERN_H_MI_S_COMPACT = "HHmmss";
  public static final String PATTERN_Y_M_D_H_MI_S_COMPACT =
      PATTERN_Y_M_D_COMPACT + PATTERN_H_MI_S_COMPACT;

  public static final long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

  private JavaTimeUtils() {
  }

  public static long milliseconds(LocalDateTime localDateTime) {
    return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
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

  public static Date parse(String str, DateFormat dateFormat, boolean throwsException) {
    return Optional.ofNullable(str)
        .filter(s -> s.trim().length() != 0)
        .flatMap(s -> Optional.ofNullable(dateFormat).map(df -> {
          try {
            return df.parse(s);
          } catch (ParseException e) {
            if (throwsException) {
              throw new IllegalArgumentException(e);
            }
            return null;
          }
        })).orElse(null);
  }

  public static Date parse(String str, DateFormat dateFormat) {
    return parse(str, dateFormat, false);
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


  public static Date innerStartOfDay(Calendar calendar) {
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public static Date innerEndOfDay(Calendar calendar) {
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    return calendar.getTime();
  }

  public static Date startOfDay(Date date) {
    return Optional.ofNullable(date).map(d -> {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(d);
      return innerStartOfDay(calendar);
    }).orElse(null);
  }

  public static Date endOfDay(Date date) {
    return Optional.ofNullable(date).map(d -> {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(d);
      return innerEndOfDay(calendar);
    }).orElse(null);
  }

  public static Date startOfWeek(Date date) {
    return Optional.ofNullable(date).map(d -> {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(d);
      calendar.setFirstDayOfWeek(Calendar.MONDAY);
      calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
      return innerStartOfDay(calendar);
    }).orElse(null);
  }

  public static Date endOfWeek(Date date) {
    return Optional.ofNullable(date).map(d -> {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(d);
      calendar.setFirstDayOfWeek(Calendar.MONDAY);
      calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
      return innerEndOfDay(calendar);
    }).orElse(null);
  }

  public static Date startOfMonth(Date date) {
    return Optional.ofNullable(date).map(d -> {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(d);
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      return innerStartOfDay(calendar);
    }).orElse(null);
  }

  public static Date endOfMonth(Date date) {
    return Optional.ofNullable(date).map(d -> {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(d);
      calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
      return innerEndOfDay(calendar);
    }).orElse(null);
  }

  public static long daysBetween(Date a, Date b, boolean strict,
      boolean absolute) {
    if (strict) {
      Calendar c = Calendar.getInstance();
      c.setTime(a);
      a = innerStartOfDay(c);
      Calendar d = Calendar.getInstance();
      d.setTime(b);
      b = innerStartOfDay(d);
    }
    long duration = a.getTime() - b.getTime();
    return absolute ? Math.abs(duration / MILLIS_PER_DAY) : duration / MILLIS_PER_DAY;
  }

  public static long daysBetween(Date a, Date b, boolean strict) {
    return daysBetween(a, b, strict, false);
  }

  public static long daysBetween(Date a, Date b) {
    return daysBetween(a, b, true);
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

  public static DateTimeFormatter yearMonthFormatter(Locale locale) {
    return dateTimeFormatter(PATTERN_Y_M, locale);
  }

  public static DateTimeFormatter yearMonthFormatter() {
    return yearMonthFormatter(Locale.getDefault());
  }

  public static DateTimeFormatter yearWeekFormatter(String pattern, int dayOfWeek, Locale locale) {
    return new DateTimeFormatterBuilder().appendPattern(pattern)
        .parseDefaulting(WeekFields.ISO.dayOfWeek(), dayOfWeek).toFormatter(locale);
  }

  public static DateTimeFormatter yearWeekFormatter(String pattern, int dayOfWeek) {
    return yearWeekFormatter(pattern, dayOfWeek, Locale.getDefault());
  }

  public static DateTimeFormatter yearWeekFormatter(String pattern) {
    return yearWeekFormatter(pattern, 1);
  }

  public static DateTimeFormatter yearWeekFormatter() {
    return yearWeekFormatter(PATTERN_Y_W);
  }

  public static DateTimeFormatter yearWeekFormatter(String pattern, Locale locale) {
    return yearWeekFormatter(pattern, 1, locale);
  }

  public static DateTimeFormatter yearWeekFormatter(int dayOfWeek, Locale locale) {
    return yearWeekFormatter(PATTERN_Y_W, dayOfWeek, locale);
  }

  public static DateTimeFormatter yearWeekFormatter(int dayOfWeek) {
    return yearWeekFormatter(dayOfWeek, Locale.getDefault());
  }

  public static DateTimeFormatter yearWeekFormatter(Locale locale) {
    return yearWeekFormatter(1, locale);
  }

  public static String format(TemporalAccessor temporalAccessor,
      DateTimeFormatter dateTimeFormatter) {
    return Optional.ofNullable(temporalAccessor)
        .flatMap(t -> Optional.ofNullable(dateTimeFormatter).map(df -> df.format(t)))
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

  public static String format(YearMonth yearMonth, Locale locale) {
    return format(yearMonth, yearMonthFormatter(locale));
  }

  public static String format(YearMonth yearMonth) {
    return format(yearMonth, Locale.getDefault());
  }

  public static String formatYearWeek(LocalDate localDate, Locale locale) {
    return format(localDate, dateTimeFormatter(PATTERN_Y_W, locale));
  }

  public static String formatYearWeek(LocalDate localDate) {
    return formatYearWeek(localDate, Locale.getDefault());
  }

  public static LocalDateTime parseLocalDateTime(String str, DateTimeFormatter dateTimeFormatter,
      boolean throwsException) {
    return Optional.ofNullable(str)
        .filter(s -> s.trim().length() != 0)
        .flatMap(s -> Optional.ofNullable(dateTimeFormatter).map(df -> {
          try {
            return LocalDateTime.parse(str, df);
          } catch (Exception e) {
            if (throwsException) {
              throw new IllegalArgumentException(e);
            }
            return null;
          }
        })).orElse(null);
  }

  public static LocalDateTime parseLocalDateTime(String str, DateTimeFormatter dateTimeFormatter) {
    return parseLocalDateTime(str, dateTimeFormatter, false);
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

  public static LocalDate parseLocalDate(String str, DateTimeFormatter dateTimeFormatter,
      boolean throwsException) {
    return Optional.ofNullable(str)
        .filter(s -> s.trim().length() != 0)
        .flatMap(s -> Optional.ofNullable(dateTimeFormatter).map(df -> {
          try {
            return LocalDate.parse(str, df);
          } catch (Exception e) {
            if (throwsException) {
              throw new IllegalArgumentException(e);
            }
            return null;
          }
        })).orElse(null);
  }

  public static LocalDate parseLocalDate(String str, DateTimeFormatter dateTimeFormatter) {
    return parseLocalDate(str, dateTimeFormatter, false);
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

  public static LocalTime parseLocalTime(String str, DateTimeFormatter dateTimeFormatter,
      boolean throwsException) {
    return Optional.ofNullable(str)
        .filter(s -> s.trim().length() != 0)
        .flatMap(s -> Optional.ofNullable(dateTimeFormatter).map(df -> {
          try {
            return LocalTime.parse(str, df);
          } catch (Exception e) {
            if (throwsException) {
              throw new IllegalArgumentException(e);
            }
            return null;
          }
        })).orElse(null);
  }

  public static LocalTime parseLocalTime(String str, DateTimeFormatter dateTimeFormatter) {
    return parseLocalTime(str, dateTimeFormatter, false);
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

  public static YearMonth parseYearMonth(String str, DateTimeFormatter dateTimeFormatter,
      boolean throwsException) {
    return Optional.ofNullable(str)
        .filter(s -> s.trim().length() != 0)
        .flatMap(s -> Optional.ofNullable(dateTimeFormatter).map(df -> {
          try {
            return YearMonth.parse(str, df);
          } catch (Exception e) {
            if (throwsException) {
              throw new IllegalArgumentException(e);
            }
            return null;
          }
        })).orElse(null);
  }

  public static YearMonth parseYearMonth(String str, DateTimeFormatter dateTimeFormatter) {
    return parseYearMonth(str, dateTimeFormatter, false);
  }

  public static YearMonth parseYearMonth(String str, String pattern, Locale locale) {
    return parseYearMonth(str, dateTimeFormatter(pattern, locale));
  }

  public static YearMonth parseYearMonth(String str, String pattern) {
    return parseYearMonth(str, dateTimeFormatter(pattern));
  }

  public static YearMonth parseYearMonth(String str, Locale locale) {
    return parseYearMonth(str, yearMonthFormatter(locale));
  }

  public static YearMonth parseYearMonth(String str) {
    return parseYearMonth(str, yearMonthFormatter());
  }

  public static LocalDate parseYearWeek(String str, DateTimeFormatter dateTimeFormatter) {
    return parseLocalDate(str, dateTimeFormatter, false);
  }

  public static LocalDate parseYearWeek(String str, String pattern, int dayOfWeek, Locale locale) {
    return parseYearWeek(str, yearWeekFormatter(pattern, dayOfWeek, locale));
  }

  public static LocalDate parseYearWeek(String str, String pattern, int dayOfWeek) {
    return parseYearWeek(str, yearWeekFormatter(pattern, dayOfWeek));
  }

  public static LocalDate parseYearWeek(String str, String pattern) {
    return parseYearWeek(str, yearWeekFormatter(pattern));
  }

  public static LocalDate parseYearWeek(String str) {
    return parseYearWeek(str, yearWeekFormatter());
  }

  public static LocalDate parseYearWeek(String str, String pattern, Locale locale) {
    return parseYearWeek(str, yearWeekFormatter(pattern, locale));
  }

  public static LocalDate parseYearWeek(String str, int dayOfWeek, Locale locale) {
    return parseYearWeek(str, yearWeekFormatter(dayOfWeek, locale));
  }

  public static LocalDate parseYearWeek(String str, int dayOfWeek) {
    return parseYearWeek(str, yearWeekFormatter(dayOfWeek));
  }

  public static LocalDate parseYearWeek(String str, Locale locale) {
    return parseYearWeek(str, yearWeekFormatter(locale));
  }

  public static LocalDateTime innerStartOfDay(LocalDateTime localDateTime) {
    return localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
  }

  public static LocalDateTime innerEndOfDay(LocalDateTime localDateTime) {
    return localDateTime.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
  }

  public static LocalDateTime startOfDay(LocalDateTime localDateTime) {
    return Optional.ofNullable(localDateTime).map(LocalDateTime::from)
        .map(JavaTimeUtils::innerStartOfDay).orElse(null);
  }

  public static LocalDateTime endOfDay(LocalDateTime localDateTime) {
    return Optional.ofNullable(localDateTime).map(LocalDateTime::from)
        .map(JavaTimeUtils::innerEndOfDay).orElse(null);
  }

  public static LocalDateTime startOfWeek(LocalDateTime localDateTime) {
    return Optional.ofNullable(localDateTime).map(LocalDateTime::from)
        .map(dt -> dt.with(DayOfWeek.MONDAY)).map(JavaTimeUtils::innerStartOfDay).orElse(null);
  }

  public static LocalDateTime endOfWeek(LocalDateTime localDateTime) {
    return Optional.ofNullable(localDateTime).map(LocalDateTime::from)
        .map(dt -> dt.with(DayOfWeek.SUNDAY)).map(JavaTimeUtils::innerEndOfDay).orElse(null);
  }

  public static LocalDateTime startOfMonth(LocalDateTime localDateTime) {
    return Optional.ofNullable(localDateTime).map(LocalDateTime::from)
        .map(dt -> dt.withDayOfMonth(1)).map(JavaTimeUtils::innerStartOfDay).orElse(null);
  }

  public static LocalDateTime endOfMonth(LocalDateTime localDateTime) {
    return Optional.ofNullable(localDateTime).map(LocalDateTime::from)
        .map(dt -> dt.withDayOfMonth(LocalDate.from(dt).lengthOfMonth()))
        .map(JavaTimeUtils::innerEndOfDay).orElse(null);
  }

  public static LocalDate startOfWeek(LocalDate localDate) {
    return Optional.ofNullable(localDate).map(LocalDate::from).map(d -> d.with(DayOfWeek.MONDAY))
        .orElse(null);
  }

  public static LocalDate endOfWeek(LocalDate localDate) {
    return Optional.ofNullable(localDate).map(LocalDate::from).map(d -> d.with(DayOfWeek.SUNDAY))
        .orElse(null);
  }

  public static LocalDate startOfMonth(LocalDate localDate) {
    return Optional.ofNullable(localDate).map(LocalDate::from).map(d -> d.withDayOfMonth(1))
        .orElse(null);
  }

  public static LocalDate endOfMonth(LocalDate localDate) {
    return Optional.ofNullable(localDate).map(LocalDate::from)
        .map(d -> d.withDayOfMonth(localDate.lengthOfMonth())).orElse(null);
  }

  public static LocalDate startOfMonth(YearMonth yearMonth) {
    return Optional.ofNullable(yearMonth).map(ym -> LocalDate.of(ym.getYear(), ym.getMonth(), 1))
        .orElse(null);
  }

  public static LocalDate endOfMonth(YearMonth yearMonth) {
    return Optional.ofNullable(yearMonth)
        .map(ym -> LocalDate.of(ym.getYear(), ym.getMonth(), ym.lengthOfMonth())).orElse(null);
  }

  private static long innerDaysBetween(Temporal a, Temporal b, boolean absolute) {
    return absolute ? Math.abs(ChronoUnit.DAYS.between(a, b)) : ChronoUnit.DAYS.between(a, b);
  }

  public static long daysBetween(LocalDateTime a, LocalDateTime b, boolean strict,
      boolean absolute) {
    if (strict) {
      return Duration.between(a, b).toDays();
    }
    return innerDaysBetween(a, b, absolute);
  }

  public static long daysBetween(LocalDateTime a, LocalDateTime b, boolean strict) {
    return daysBetween(a, b, strict, false);
  }

  public static long daysBetween(LocalDateTime a, LocalDateTime b) {
    return daysBetween(a, b, true);
  }

  public static long daysBetween(LocalDate a, LocalDate b, boolean absolute) {
    return innerDaysBetween(a, b, absolute);
  }

  public static long daysBetween(LocalDate a, LocalDate b) {
    return daysBetween(a, b, false);
  }

}
