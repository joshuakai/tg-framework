package com.tg.framework.commons.util;

import com.tg.framework.commons.lang.StringOptional;
import java.util.Arrays;

public class MaskUtils {

  private static final String DEFAULT_VALUE = "";
  private static final char DEFAULT_SYMBOL = '*';
  private static final int DEFAULT_SYMBOL_LENGTH = 3;
  private static final int DEFAULT_MIN_MASK_LENGTH = 3;
  private static final int DEFAULT_MAX_LEFT_LENGTH = 3;
  private static final int DEFAULT_MAX_RIGHT_LENGTH = 3;

  private MaskUtils() {
  }

  public static String maskLeft(String rawValue, int minMaskLength, int maxRightLength, char symbol,
      int symbolLength, String defaultValue) {
    return StringOptional.ofNullable(rawValue).map(str -> {
      int len = str.length();
      if (len <= minMaskLength) {
        return getSymbols(symbol, symbolLength);
      }
      if (len <= minMaskLength + maxRightLength) {
        return getSymbols(symbol, symbolLength) + str.substring(len - minMaskLength);
      }
      return getSymbols(symbol, symbolLength) + str.substring(len - maxRightLength);
    }).orElse(defaultValue);
  }

  public static String maskLeft(String rawValue, int minMaskLength, int maxRightLength, char symbol,
      int symbolLength) {
    return maskLeft(rawValue, minMaskLength, maxRightLength, symbol, symbolLength, DEFAULT_VALUE);
  }

  public static String maskLeft(String rawValue, int minMaskLength, int maxRightLength) {
    return maskLeft(rawValue, minMaskLength, maxRightLength, DEFAULT_SYMBOL, DEFAULT_SYMBOL_LENGTH);
  }

  public static String maskLeft(String rawValue) {
    return maskLeft(rawValue, DEFAULT_MIN_MASK_LENGTH, DEFAULT_MAX_RIGHT_LENGTH);
  }

  public static String maskCenter(String rawValue, int minMaskLength, int maxLeftLength,
      int maxRightLength, char symbol, int symbolLength, String defaultValue) {
    return StringOptional.ofNullable(rawValue).map(str -> {
      int len = str.length();
      if (len <= minMaskLength) {
        return getSymbols(symbol, symbolLength);
      }
      if (len <= maxLeftLength + minMaskLength + maxRightLength) {
        if (len <= minMaskLength + maxRightLength) {
          return getSymbols(symbol, symbolLength) + str.substring(len - minMaskLength);
        } else {
          return str.substring(0, len - minMaskLength - maxRightLength) + getSymbols(symbol,
              symbolLength) + str.substring(len - minMaskLength);
        }
      }
      return str.substring(0, maxLeftLength) + getSymbols(symbol, symbolLength) + str
          .substring(len - maxRightLength);
    }).orElse(defaultValue);
  }

  public static String maskCenter(String rawValue, int minMaskLength, int maxLeftLength,
      int maxRightLength, char symbol, int symbolLength) {
    return maskCenter(rawValue, minMaskLength, maxLeftLength, maxRightLength, symbol, symbolLength,
        DEFAULT_VALUE);
  }

  public static String maskCenter(String rawValue, int minMaskLength, int maxLeftLength,
      int maxRightLength) {
    return maskCenter(rawValue, minMaskLength, maxLeftLength, maxRightLength, DEFAULT_SYMBOL,
        DEFAULT_SYMBOL_LENGTH);
  }

  public static String maskCenter(String rawValue) {
    return maskCenter(rawValue, DEFAULT_MIN_MASK_LENGTH, DEFAULT_MAX_LEFT_LENGTH,
        DEFAULT_MAX_RIGHT_LENGTH);
  }

  public static String maskRight(String rawValue, int minMaskLength, int maxLeftLength, char symbol,
      int symbolLength, String defaultValue) {
    return StringOptional.ofNullable(rawValue).map(str -> {
      int len = str.length();
      if (len <= minMaskLength) {
        return getSymbols(symbol, symbolLength);
      }
      if (len <= minMaskLength + maxLeftLength) {
        return str.substring(0, len - minMaskLength) + getSymbols(symbol, symbolLength);
      }
      return str.substring(0, maxLeftLength) + getSymbols(symbol, symbolLength);
    }).orElse(defaultValue);
  }

  public static String maskRight(String rawValue, int minMaskLength, int maxLeftLength, char symbol,
      int symbolLength) {
    return maskRight(rawValue, minMaskLength, maxLeftLength, symbol, symbolLength, DEFAULT_VALUE);
  }

  public static String maskRight(String rawValue, int minMaskLength, int maxLeftLength) {
    return maskRight(rawValue, minMaskLength, maxLeftLength, DEFAULT_SYMBOL, DEFAULT_SYMBOL_LENGTH);
  }

  public static String maskRight(String rawValue) {
    return maskRight(rawValue, DEFAULT_MIN_MASK_LENGTH, DEFAULT_MAX_LEFT_LENGTH);
  }

  private static String getSymbols(char symbol, int length) {
    char[] symbols = new char[length];
    Arrays.fill(symbols, symbol);
    return new String(symbols);
  }

}
