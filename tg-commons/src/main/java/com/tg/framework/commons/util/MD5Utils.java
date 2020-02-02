package com.tg.framework.commons.util;

import static com.tg.framework.commons.util.StringByteTransformer.string2Bytes;

import java.util.Optional;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class MD5Utils {

  private MD5Utils() {
  }

  public static byte[] md5(byte[] bytes) {
    return Optional.ofNullable(bytes).map(DigestUtils::md5).orElse(null);
  }

  public static byte[] md5(String str) {
    return md5(string2Bytes(str));
  }

  public static String md5Hex(byte[] bytes) {
    return Optional.ofNullable(bytes).map(DigestUtils::md5Hex).orElse(null);
  }

  public static String md5Hex(String str) {
    return md5Hex(string2Bytes(str));
  }

  public static String md5HexUpperCase(byte[] bytes) {
    return Optional.ofNullable(md5Hex(bytes)).map(String::toUpperCase).orElse(null);
  }

  public static String md5HexUpperCase(String str) {
    return Optional.ofNullable(md5Hex(str)).map(String::toUpperCase).orElse(null);
  }

  public static boolean verify(String str, String answer) {
    return StringUtils.equals(md5Hex(str), answer);
  }

  public static boolean verifyIgnoreCase(String str, String answer) {
    return StringUtils.equalsIgnoreCase(md5Hex(str), answer);
  }

}
