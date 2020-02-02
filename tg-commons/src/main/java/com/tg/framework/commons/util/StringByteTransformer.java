package com.tg.framework.commons.util;

import java.nio.charset.Charset;
import java.util.Optional;

public class StringByteTransformer {

  private StringByteTransformer() {
  }

  public static byte[] string2Bytes(String str, Charset charset) {
    return Optional.ofNullable(str).map(v -> v.getBytes(charset)).orElse(null);
  }

  public static byte[] string2Bytes(String str) {
    return string2Bytes(str, Charset.defaultCharset());
  }

  public static String bytes2String(byte[] bytes, Charset charset) {
    return Optional.ofNullable(bytes).map(v -> new String(v, charset)).orElse(null);
  }

  public static String bytes2String(byte[] bytes) {
    return bytes2String(bytes, Charset.defaultCharset());
  }

}
