package com.tg.framework.commons.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import org.apache.commons.lang3.math.NumberUtils;

public class CNYUtils {

  private static final NumberFormat FEN_FORMAT = NumberFormat.getInstance();
  private static final NumberFormat JIAO_FORMAT = NumberFormat.getInstance();
  private static final NumberFormat YUAN_FORMAT = NumberFormat.getInstance();

  static {
    FEN_FORMAT.setGroupingUsed(false);
    FEN_FORMAT.setMaximumFractionDigits(0);
    JIAO_FORMAT.setGroupingUsed(false);
    JIAO_FORMAT.setMaximumFractionDigits(1);
    JIAO_FORMAT.setMinimumFractionDigits(1);
    YUAN_FORMAT.setGroupingUsed(false);
    YUAN_FORMAT.setMaximumFractionDigits(2);
    YUAN_FORMAT.setMinimumFractionDigits(2);
  }

  public enum CNYUnit {
    FEN(1), JIAO(10), YUAN(100);
    private int scale;

    CNYUnit(int scale) {
      this.scale = scale;
    }

    public int getScale() {
      return scale;
    }
  }

  private CNYUtils() {
  }

  public static String format(double amount, CNYUnit unit, CNYUnit targetUnit) {
    return matchesNumberFormat(targetUnit).format(convertAmount(amount, unit, targetUnit));
  }

  public static String formatYuanAsFen(double amount) {
    return format(amount, CNYUnit.YUAN, CNYUnit.FEN);
  }

  public static String formatYuanAsJiao(double amount) {
    return format(amount, CNYUnit.YUAN, CNYUnit.JIAO);
  }

  public static String formatYuanAsYuan(double amount) {
    return format(amount, CNYUnit.YUAN, CNYUnit.YUAN);
  }

  public static String formatJiaoAsFen(double amount) {
    return format(amount, CNYUnit.JIAO, CNYUnit.FEN);
  }

  public static String formatJiaoAsJiao(double amount) {
    return format(amount, CNYUnit.JIAO, CNYUnit.JIAO);
  }

  public static String formatJiaoAsYuan(double amount) {
    return format(amount, CNYUnit.JIAO, CNYUnit.YUAN);
  }

  public static String formatFenAsFen(double amount) {
    return format(amount, CNYUnit.FEN, CNYUnit.FEN);
  }

  public static String formatFenAsJiao(double amount) {
    return format(amount, CNYUnit.FEN, CNYUnit.JIAO);
  }

  public static String formatFenAsYuan(double amount) {
    return format(amount, CNYUnit.FEN, CNYUnit.YUAN);
  }

  public static double parse(String str, CNYUnit unit, CNYUnit targetUnit) {
    return convertAmount(NumberUtils.toDouble(str), unit, targetUnit);
  }

  public static double parseYuanAsFen(String str) {
    return parse(str, CNYUnit.YUAN, CNYUnit.FEN);
  }

  public static double parseYuanAsJiao(String str) {
    return parse(str, CNYUnit.YUAN, CNYUnit.JIAO);
  }

  public static double parseYuanAsYuan(String str) {
    return parse(str, CNYUnit.YUAN, CNYUnit.YUAN);
  }

  public static double parseJiaoAsFen(String str) {
    return parse(str, CNYUnit.JIAO, CNYUnit.FEN);
  }

  public static double parseJiaoAsJiao(String str) {
    return parse(str, CNYUnit.JIAO, CNYUnit.JIAO);
  }

  public static double parseJiaoAsYuan(String str) {
    return parse(str, CNYUnit.JIAO, CNYUnit.YUAN);
  }

  public static double parseFenAsFen(String str) {
    return parse(str, CNYUnit.FEN, CNYUnit.FEN);
  }

  public static double parseFenAsJiao(String str) {
    return parse(str, CNYUnit.FEN, CNYUnit.JIAO);
  }

  public static double parseFenAsYuan(String str) {
    return parse(str, CNYUnit.FEN, CNYUnit.YUAN);
  }

  public static double sum(double... values) {
    if (values == null || values.length == 0) {
      return 0D;
    }
    if (values.length == 1) {
      return values[0];
    }
    BigDecimal sum = BigDecimal.valueOf(values[0]);
    for (int i = 1; i < values.length; i++) {
      sum = sum.add(BigDecimal.valueOf(values[i]));
    }
    return sum.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  private static double convertAmount(double amount, CNYUnit unit, CNYUnit targetUnit) {
    if (unit == targetUnit) {
      return amount;
    }
    BigDecimal scale = BigDecimal.valueOf(unit.getScale())
        .divide(BigDecimal.valueOf(targetUnit.getScale()), 2, RoundingMode.HALF_UP);
    return BigDecimal.valueOf(amount).multiply(scale).doubleValue();
  }

  private static NumberFormat matchesNumberFormat(CNYUnit unit) {
    switch (unit) {
      case FEN:
        return FEN_FORMAT;
      case JIAO:
        return JIAO_FORMAT;
      case YUAN:
        return YUAN_FORMAT;
      default:
        throw new IllegalArgumentException("Unknown CNYUnit");
    }
  }

}
