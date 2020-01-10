package com.tg.framework.commons.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * TODO AES DES...
 */
public class CryptoUtils {

  private static final String ALGORITHM_RSA = "RSA";

  private static final int ALGORITHM_RSA_MAX_ENCRYPT_BLOCK = 117;
  private static final int ALGORITHM_RSA_MAX_DECRYPT_BLOCK = 128;

  private CryptoUtils() {
  }

  public static String encryptByMD5(byte[] bytes) {
    return DigestUtils.md5Hex(bytes);
  }

  public static String encryptByMD5(String str, String charset) {
    return encryptByMD5(string2Bytes(str, charset));
  }

  public static String encryptByMD5(String str) {
    return encryptByMD5(string2Bytes(str));
  }

  public static byte[] encryptByRSA(byte[] bytes, Key key) {
    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      int length = bytes.length;
      if (length <= ALGORITHM_RSA_MAX_ENCRYPT_BLOCK) {
        return cipher.doFinal(bytes);
      }
      int offset = 0;
      try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        while (offset < length) {
          out.write(cipher.doFinal(bytes, offset, ALGORITHM_RSA_MAX_ENCRYPT_BLOCK));
          offset += ALGORITHM_RSA_MAX_ENCRYPT_BLOCK;
        }
        return out.toByteArray();
      }
    } catch (NoSuchAlgorithmException | IOException e) {
      throw new IllegalStateException(e);
    } catch (NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static byte[] encryptByRSA(String str, String charset, Key key) {
    return encryptByRSA(string2Bytes(str, charset), key);
  }

  public static byte[] encryptByRSA(String str, Key key) {
    return encryptByRSA(string2Bytes(str), key);
  }

  public static byte[] encryptByRSA(byte[] bytes, byte[] keyBytes) {
    try {
      return encryptByRSA(bytes,
          KeyFactory.getInstance(ALGORITHM_RSA).generatePrivate(new PKCS8EncodedKeySpec(keyBytes)));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static byte[] encryptByRSA(String str, String charset, byte[] keyBytes) {
    return encryptByRSA(string2Bytes(str, charset), keyBytes);
  }

  public static byte[] encryptByRSA(String str, byte[] keyBytes) {
    return encryptByRSA(string2Bytes(str), keyBytes);
  }

  public static byte[] encryptByRSA(byte[] bytes, String key) {
    return encryptByRSA(bytes, getRSAKeyBytes(key));
  }

  public static byte[] encryptByRSA(String str, String charset, String key) {
    return encryptByRSA(string2Bytes(str, charset), key);
  }

  public static byte[] encryptByRSA(String str, String key) {
    return encryptByRSA(string2Bytes(str), key);
  }

  public static String encryptAsStringByRSA(byte[] bytes, Key key) {
    return encodeToStringByBase64(encryptByRSA(bytes, key));
  }

  public static String encryptAsStringByRSA(String str, String charset, Key key) {
    return encodeToStringByBase64(encryptByRSA(str, charset, key));
  }

  public static String encryptAsStringByRSA(String str, Key key) {
    return encodeToStringByBase64(encryptByRSA(str, key));
  }

  public static String encryptAsStringByRSA(byte[] bytes, byte[] keyBytes) {
    return encodeToStringByBase64(encryptByRSA(bytes, keyBytes));
  }

  public static String encryptAsStringByRSA(String str, String charset, byte[] keyBytes) {
    return encodeToStringByBase64(encryptByRSA(str, charset, keyBytes));
  }

  public static String encryptAsStringByRSA(String str, byte[] keyBytes) {
    return encodeToStringByBase64(encryptByRSA(str, keyBytes));
  }

  public static String encryptAsStringByRSA(byte[] bytes, String key) {
    return encodeToStringByBase64(encryptByRSA(bytes, key));
  }

  public static String encryptAsStringByRSA(String str, String charset, String key) {
    return encodeToStringByBase64(encryptByRSA(str, charset, key));
  }

  public static String encryptAsStringByRSA(String str, String key) {
    return encodeToStringByBase64(encryptByRSA(str, key));
  }

  public static byte[] decryptByRSA(byte[] bytes, Key key) {
    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
      cipher.init(Cipher.DECRYPT_MODE, key);
      int length = bytes.length;
      if (length <= ALGORITHM_RSA_MAX_DECRYPT_BLOCK) {
        return cipher.doFinal(bytes);
      }
      int offset = 0;
      try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        while (offset < length) {
          out.write(cipher.doFinal(bytes, offset, ALGORITHM_RSA_MAX_DECRYPT_BLOCK));
          offset += ALGORITHM_RSA_MAX_DECRYPT_BLOCK;
        }
        return out.toByteArray();
      }
    } catch (NoSuchAlgorithmException | IOException e) {
      throw new IllegalStateException(e);
    } catch (NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static byte[] decryptByRSA(String str, Key key) {
    return decryptByRSA(decodeByBase64(str), key);
  }

  public static byte[] decryptByRSA(byte[] bytes, byte[] keyBytes) {
    try {
      return decryptByRSA(bytes,
          KeyFactory.getInstance(ALGORITHM_RSA).generatePublic(new X509EncodedKeySpec(keyBytes)));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static byte[] decryptByRSA(String str, byte[] keyBytes) {
    return decryptByRSA(decodeByBase64(str), keyBytes);
  }

  public static byte[] decryptByRSA(byte[] bytes, String key) {
    return decryptByRSA(bytes, getRSAKeyBytes(key));
  }

  public static byte[] decryptByRSA(String str, String key) {
    return decryptByRSA(decodeByBase64(str), key);
  }

  public static String decryptAsStringByRSA(byte[] bytes, Key key) {
    return bytes2String(decryptByRSA(bytes, key));
  }

  public static String decryptAsStringByRSA(String str, Key key) {
    return bytes2String(decryptByRSA(str, key));
  }

  public static String decryptAsStringByRSA(byte[] bytes, byte[] keyBytes) {
    return bytes2String(decryptByRSA(bytes, keyBytes));
  }

  public static String decryptAsStringByRSA(String str, byte[] keyBytes) {
    return bytes2String(decryptByRSA(str, keyBytes));
  }

  public static String decryptAsStringByRSA(byte[] bytes, String key) {
    return bytes2String(decryptByRSA(bytes, key));
  }

  public static String decryptAsStringByRSA(String str, String key) {
    return bytes2String(decryptByRSA(str, key));
  }

  public static boolean verifyByRSA(String str, String key, String answer) {
    return StringUtils.equals(decryptAsStringByRSA(str, key), answer);
  }

  private static String encodeToStringByBase64(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  private static byte[] decodeByBase64(String str) {
    return Base64.getDecoder().decode(str);
  }

  private static byte[] string2Bytes(String str, Charset charset) {
    return str.getBytes(Optional.ofNullable(charset).orElse(Charset.defaultCharset()));
  }

  private static byte[] string2Bytes(String str, String charset) {
    return string2Bytes(str, Optional.ofNullable(charset).map(Charset::forName).orElse(null));
  }

  private static byte[] string2Bytes(String str) {
    return string2Bytes(str, Charset.defaultCharset());
  }

  private static String bytes2String(byte[] bytes, Charset charset) {
    return new String(bytes, Optional.ofNullable(charset).orElse(Charset.defaultCharset()));
  }

  private static String bytes2String(byte[] bytes) {
    return bytes2String(bytes, Charset.defaultCharset());
  }

  private static byte[] getRSAKeyBytes(String key) {
    return decodeByBase64(key.replaceAll(System.lineSeparator(), StringUtils.EMPTY));
  }

}
