package com.tg.framework.commons.util;

import static com.tg.framework.commons.util.Base64Utils.decode;
import static com.tg.framework.commons.util.Base64Utils.encode2String;
import static com.tg.framework.commons.util.StringByteTransformer.string2Bytes;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class SignatureUtils {

  private SignatureUtils() {
  }

  public static byte[] sign(String algorithm, byte[] bytes, PrivateKey privateKey) {
    try {
      Signature signature = Signature.getInstance(algorithm);
      signature.initSign(privateKey);
      signature.update(bytes);
      return signature.sign();
    } catch (NoSuchAlgorithmException | SignatureException e) {
      throw new IllegalStateException(e);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static byte[] sign(String algorithm, String str, PrivateKey privateKey) {
    return sign(algorithm, string2Bytes(str), privateKey);
  }

  public static String sign2String(String algorithm, byte[] bytes, PrivateKey privateKey) {
    return encode2String(sign(algorithm, bytes, privateKey));
  }

  public static String sign2String(String algorithm, String str, PrivateKey privateKey) {
    return encode2String(sign(algorithm, str, privateKey));
  }


  public static boolean verify(String algorithm, byte[] bytes, byte[] signBytes,
      PublicKey publicKey) {
    try {
      Signature signature = Signature.getInstance(algorithm);
      signature.initVerify(publicKey);
      signature.update(bytes);
      return signature.verify(signBytes);
    } catch (NoSuchAlgorithmException | SignatureException e) {
      throw new IllegalStateException(e);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static boolean verify(String algorithm, String str, String signStr, PublicKey publicKey) {
    return verify(algorithm, string2Bytes(str), decode(signStr), publicKey);
  }

  public static boolean verify(String algorithm, byte[] bytes, String signStr,
      PublicKey publicKey) {
    return verify(algorithm, bytes, decode(signStr), publicKey);
  }

  public static boolean verify(String algorithm, String str, byte[] signBytes,
      PublicKey publicKey) {
    return verify(algorithm, string2Bytes(str), signBytes, publicKey);
  }
}
