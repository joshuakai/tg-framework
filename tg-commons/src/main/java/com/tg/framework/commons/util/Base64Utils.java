package com.tg.framework.commons.util;

import static com.tg.framework.commons.util.StringByteTransformer.bytes2String;
import static com.tg.framework.commons.util.StringByteTransformer.string2Bytes;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Optional;

public class Base64Utils {

  private static final char URL_SPECIAL_CHAR = '_';

  private Base64Utils() {
  }

  public static byte[] encode(Encoder encoder, byte[] bytes) {
    return Optional.ofNullable(bytes).map(encoder::encode).orElse(null);
  }

  public static byte[] encode(byte[] bytes) {
    return encode(Base64.getEncoder(), bytes);
  }

  public static byte[] encode(String str) {
    return encode(string2Bytes(str));
  }

  public static byte[] mimeEncode(byte[] bytes) {
    return encode(Base64.getMimeEncoder(), bytes);
  }

  public static byte[] mimeEncode(String str) {
    return mimeEncode(string2Bytes(str));
  }

  public static byte[] urlEncode(byte[] bytes) {
    return encode(Base64.getUrlEncoder(), bytes);
  }

  public static byte[] urlEncode(String str) {
    return urlEncode(string2Bytes(str));
  }

  public static String encode2String(Encoder encoder, byte[] bytes) {
    return encoder.encodeToString(bytes);
  }

  public static String encode2String(byte[] bytes) {
    return encode2String(Base64.getEncoder(), bytes);
  }

  public static String encode2String(String str) {
    return encode2String(string2Bytes(str));
  }

  public static String mimeEncode2String(byte[] bytes) {
    return encode2String(Base64.getMimeEncoder(), bytes);
  }

  public static String mimeEncode2String(String str) {
    return mimeEncode2String(string2Bytes(str));
  }

  public static String urlEncode2String(byte[] bytes) {
    return encode2String(Base64.getUrlEncoder(), bytes);
  }

  public static String urlEncode2String(String str) {
    return urlEncode2String(string2Bytes(str));
  }

  public static byte[] decode(Decoder decoder, byte[] bytes) {
    return Optional.ofNullable(bytes).map(decoder::decode).orElse(null);
  }

  public static byte[] decode(byte[] bytes) {
    return decode(Base64.getDecoder(), bytes);
  }

  public static byte[] decode(String str) {
    return decode(string2Bytes(str));
  }

  public static byte[] mimeDecode(byte[] bytes) {
    return decode(Base64.getMimeDecoder(), bytes);
  }

  public static byte[] mimeDecode(String str) {
    return mimeDecode(string2Bytes(str));
  }

  public static byte[] urlDecode(byte[] bytes) {
    return decode(Base64.getUrlDecoder(), bytes);
  }

  public static byte[] urlDecode(String str) {
    return urlDecode(string2Bytes(str));
  }

  public static byte[] smartDecode(String str) {
    return Optional.ofNullable(str)
        .filter(v -> v.indexOf(URL_SPECIAL_CHAR) != -1)
        .map(Base64Utils::urlDecode)
        .orElseGet(() -> mimeDecode(str));
  }

  public static String decode2String(Decoder decoder, byte[] bytes) {
    return Optional.ofNullable(decode(decoder, bytes)).map(String::new).orElse(null);
  }

  public static String decode2String(byte[] bytes) {
    return decode2String(Base64.getDecoder(), bytes);
  }

  public static String decode2String(String str) {
    return decode2String(string2Bytes(str));
  }

  public static String mimeDecode2String(byte[] bytes) {
    return decode2String(Base64.getMimeDecoder(), bytes);
  }

  public static String mimeDecode2String(String str) {
    return mimeDecode2String(string2Bytes(str));
  }

  public static String urlDecode2String(byte[] bytes) {
    return decode2String(Base64.getUrlDecoder(), bytes);
  }

  public static String urlDecode2String(String str) {
    return urlDecode2String(string2Bytes(str));
  }

  public static String smartDecode2String(String str) {
    return bytes2String(smartDecode(str));
  }

}
