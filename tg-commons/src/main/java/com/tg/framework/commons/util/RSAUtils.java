package com.tg.framework.commons.util;

import static com.tg.framework.commons.util.Base64Utils.decode;
import static com.tg.framework.commons.util.Base64Utils.encode2String;
import static com.tg.framework.commons.util.Base64Utils.smartDecode;
import static com.tg.framework.commons.util.StringByteTransformer.bytes2String;
import static com.tg.framework.commons.util.StringByteTransformer.string2Bytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.lang3.StringUtils;

public class RSAUtils {

  private static final String ALGORITHM = "RSA";
  private static final int DEFAULT_MODULUS = 2048;
  private static final String CIPHER_TRANSFORMATION = "RSA/ECB/PKCS1Padding";

  private RSAUtils() {
  }

  public enum RSASignatureAlgorithm {
    MD2withRSA,
    MD5andSHA1withRSA,
    MD5withRSA,
    NONEwithRSA,
    SHA1withRSA,
    SHA224withRSA,
    SHA256withRSA,
    SHA384withRSA,
    SHA512withRSA
  }

  public static KeyPair randomKeyPair(int modulus) {
    KeyPairGenerator kpg;
    try {
      kpg = KeyPairGenerator.getInstance(ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
    kpg.initialize(modulus, new SecureRandom());
    return kpg.generateKeyPair();
  }

  public static KeyPair randomKeyPair() {
    return randomKeyPair(DEFAULT_MODULUS);
  }

  public static RSAPrivateKey toPrivateKey(byte[] privateKeyBytes) {
    try {
      return (RSAPrivateKey) KeyFactory.getInstance(ALGORITHM)
          .generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static RSAPrivateKey toPrivateKey(String privateKey) {
    return toPrivateKey(smartDecode(privateKey));
  }

  public static RSAPublicKey toPublicKey(byte[] publickKeyBytes) {
    try {
      return (RSAPublicKey) KeyFactory.getInstance(ALGORITHM)
          .generatePublic(new X509EncodedKeySpec(publickKeyBytes));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static RSAPublicKey toPublicKey(String publicKey) {
    return toPublicKey(smartDecode(publicKey));
  }

  public static <T extends Key & RSAKey> byte[] encrypt(byte[] bytes, T rsaKey) {
    return blockingDoFinal(CIPHER_TRANSFORMATION, Cipher.ENCRYPT_MODE, rsaKey, bytes,
        (rsaKey.getModulus().bitLength() >> 3) - 11);
  }

  public static <T extends Key & RSAKey> byte[] encrypt(String str, T rsaKey) {
    return encrypt(string2Bytes(str), rsaKey);
  }

  public static byte[] privateEncrypt(byte[] bytes, byte[] privateKeyBytes) {
    return encrypt(bytes, toPrivateKey(privateKeyBytes));
  }

  public static byte[] privateEncrypt(String str, byte[] privateKeyBytes) {
    return encrypt(str, toPrivateKey(privateKeyBytes));
  }

  public static byte[] privateEncrypt(byte[] bytes, String privateKey) {
    return encrypt(bytes, toPrivateKey(privateKey));
  }

  public static byte[] privateEncrypt(String str, String privateKey) {
    return encrypt(str, toPrivateKey(privateKey));
  }

  public static byte[] publicEncrypt(byte[] bytes, byte[] publicKeyBytes) {
    return encrypt(bytes, toPublicKey(publicKeyBytes));
  }

  public static byte[] publicEncrypt(String str, byte[] publicKeyBytes) {
    return encrypt(str, toPublicKey(publicKeyBytes));
  }

  public static byte[] publicEncrypt(byte[] bytes, String publicKey) {
    return encrypt(bytes, toPublicKey(publicKey));
  }

  public static byte[] publicEncrypt(String str, String publicKey) {
    return encrypt(str, toPublicKey(publicKey));
  }

  public static <T extends Key & RSAKey> String encrypt2String(byte[] bytes, T rsaKey) {
    return encode2String(encrypt(bytes, rsaKey));
  }

  public static <T extends Key & RSAKey> String encrypt2String(String str, T rsaKey) {
    return encode2String(encrypt(str, rsaKey));
  }

  public static String privateEncrypt2String(byte[] bytes, byte[] privateKeyBytes) {
    return encode2String(privateEncrypt(bytes, privateKeyBytes));
  }

  public static String privateEncrypt2String(String str, byte[] privateKeyBytes) {
    return encode2String(privateEncrypt(str, privateKeyBytes));
  }

  public static String privateEncrypt2String(byte[] bytes, String privateKey) {
    return encode2String(privateEncrypt(bytes, privateKey));
  }

  public static String privateEncrypt2String(String str, String privateKey) {
    return encode2String(privateEncrypt(str, privateKey));
  }

  public static String publicEncrypt2String(byte[] bytes, byte[] publicKeyBytes) {
    return encode2String(publicEncrypt(bytes, publicKeyBytes));
  }

  public static String publicEncrypt2String(String str, byte[] publicKeyBytes) {
    return encode2String(publicEncrypt(str, publicKeyBytes));
  }

  public static String publicEncrypt2String(byte[] bytes, String publicKey) {
    return encode2String(publicEncrypt(bytes, publicKey));
  }

  public static String publicEncrypt2String(String str, String publicKey) {
    return encode2String(publicEncrypt(str, publicKey));
  }

  public static <T extends Key & RSAKey> byte[] decrypt(byte[] bytes, T rsaKey) {
    return blockingDoFinal(CIPHER_TRANSFORMATION, Cipher.DECRYPT_MODE, rsaKey, bytes,
        rsaKey.getModulus().bitLength() >> 3);
  }

  public static <T extends Key & RSAKey> byte[] decrypt(String str, T rsaKey) {
    return decrypt(smartDecode(str), rsaKey);
  }

  public static byte[] privateDecrypt(byte[] bytes, byte[] privateKeyBytes) {
    return decrypt(bytes, toPrivateKey(privateKeyBytes));
  }

  public static byte[] privateDecrypt(String str, byte[] privateKeyBytes) {
    return decrypt(str, toPrivateKey(privateKeyBytes));
  }

  public static byte[] privateDecrypt(byte[] bytes, String privateKey) {
    return decrypt(bytes, toPrivateKey(privateKey));
  }

  public static byte[] privateDecrypt(String str, String privateKey) {
    return decrypt(str, toPrivateKey(privateKey));
  }

  public static byte[] publicDecrypt(byte[] bytes, byte[] publicKeyBytes) {
    return decrypt(bytes, toPublicKey(publicKeyBytes));
  }

  public static byte[] publicDecrypt(String str, byte[] publicKeyBytes) {
    return decrypt(str, toPublicKey(publicKeyBytes));
  }

  public static byte[] publicDecrypt(byte[] bytes, String publicKey) {
    return decrypt(bytes, toPublicKey(publicKey));
  }

  public static byte[] publicDecrypt(String str, String publicKey) {
    return decrypt(str, toPublicKey(publicKey));
  }

  public static <T extends Key & RSAKey> String decrypt2String(byte[] bytes, T rsaKey) {
    return bytes2String(decrypt(bytes, rsaKey));
  }

  public static <T extends Key & RSAKey> String decrypt2String(String str, T rsaKey) {
    return bytes2String(decrypt(str, rsaKey));
  }

  public static String privateDecrypt2String(byte[] bytes, byte[] privateKeyBytes) {
    return bytes2String(privateDecrypt(bytes, privateKeyBytes));
  }

  public static String privateDecrypt2String(String str, byte[] privateKeyBytes) {
    return bytes2String(privateDecrypt(str, privateKeyBytes));
  }

  public static String privateDecrypt2String(byte[] bytes, String privateKey) {
    return bytes2String(privateDecrypt(bytes, privateKey));
  }

  public static String privateDecrypt2String(String str, String privateKey) {
    return bytes2String(privateDecrypt(str, privateKey));
  }

  public static String publicDecrypt2String(byte[] bytes, byte[] publicKeyBytes) {
    return bytes2String(publicDecrypt(bytes, publicKeyBytes));
  }

  public static String publicDecrypt2String(String str, byte[] publicKeyBytes) {
    return bytes2String(publicDecrypt(str, publicKeyBytes));
  }

  public static String publicDecrypt2String(byte[] bytes, String publicKey) {
    return bytes2String(publicDecrypt(bytes, publicKey));
  }

  public static String publicDecrypt2String(String str, String publicKey) {
    return bytes2String(publicDecrypt(str, publicKey));
  }

  public static boolean publicVerify(String str, String answer, String publicKey) {
    return StringUtils.equals(str, publicDecrypt2String(answer, publicKey));
  }

  public static boolean privateVerify(String str, String answer, String privateKey) {
    return StringUtils.equals(str, privateDecrypt2String(answer, privateKey));
  }

  public static byte[] sign(RSASignatureAlgorithm algorithm, byte[] bytes,
      RSAPrivateKey privateKey) {
    return SignatureUtils.sign(algorithm.name(), bytes, privateKey);
  }

  public static byte[] sign(RSASignatureAlgorithm algorithm, String str, RSAPrivateKey privateKey) {
    return sign(algorithm, string2Bytes(str), privateKey);
  }

  public static String sign2String(RSASignatureAlgorithm algorithm, byte[] bytes,
      RSAPrivateKey privateKey) {
    return encode2String(sign(algorithm, bytes, privateKey));
  }

  public static String sign2String(RSASignatureAlgorithm algorithm, String str,
      RSAPrivateKey privateKey) {
    return encode2String(sign(algorithm, str, privateKey));
  }

  public static byte[] sign(RSASignatureAlgorithm algorithm, byte[] bytes, byte[] privateKeyBytes) {
    return sign(algorithm, bytes, toPrivateKey(privateKeyBytes));
  }

  public static byte[] sign(RSASignatureAlgorithm algorithm, String str, byte[] privateKeyBytes) {
    return sign(algorithm, string2Bytes(str), privateKeyBytes);
  }

  public static byte[] sign(RSASignatureAlgorithm algorithm, byte[] bytes, String privateKey) {
    return sign(algorithm, bytes, toPrivateKey(privateKey));
  }

  public static byte[] sign(RSASignatureAlgorithm algorithm, String str, String privateKey) {
    return sign(algorithm, string2Bytes(str), privateKey);
  }

  public static String sign2String(RSASignatureAlgorithm algorithm, byte[] bytes,
      byte[] privateKeyBytes) {
    return encode2String(sign(algorithm, bytes, privateKeyBytes));
  }

  public static String sign2String(RSASignatureAlgorithm algorithm, String str,
      byte[] privateKeyBytes) {
    return encode2String(sign(algorithm, str, privateKeyBytes));
  }

  public static String sign2String(RSASignatureAlgorithm algorithm, byte[] bytes,
      String privateKey) {
    return encode2String(sign(algorithm, bytes, privateKey));
  }

  public static String sign2String(RSASignatureAlgorithm algorithm, String str, String privateKey) {
    return encode2String(sign(algorithm, str, privateKey));
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, byte[] bytes, byte[] signBytes,
      RSAPublicKey publicKey) {
    return SignatureUtils.verify(algorithm.name(), bytes, signBytes, publicKey);
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, String str, String signStr,
      RSAPublicKey publicKey) {
    return verify(algorithm, string2Bytes(str), decode(signStr), publicKey);
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, byte[] bytes, String signStr,
      RSAPublicKey publicKey) {
    return verify(algorithm, bytes, decode(signStr), publicKey);
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, String str, byte[] signBytes,
      RSAPublicKey publicKey) {
    return verify(algorithm, string2Bytes(str), signBytes, publicKey);
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, byte[] bytes, byte[] signBytes,
      byte[] publicKeyBytes) {
    return verify(algorithm, bytes, signBytes, toPublicKey(publicKeyBytes));
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, String str, String signStr,
      byte[] publicKeyBytes) {
    return verify(algorithm, string2Bytes(str), decode(signStr), publicKeyBytes);
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, byte[] bytes, byte[] signBytes,
      String publicKey) {
    return verify(algorithm, bytes, signBytes, toPublicKey(publicKey));
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, String str, String signStr,
      String publicKey) {
    return verify(algorithm, string2Bytes(str), decode(signStr), publicKey);
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, byte[] bytes, String signStr,
      byte[] publicKeyBytes) {
    return verify(algorithm, bytes, decode(signStr), publicKeyBytes);
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, String str, byte[] signBytes,
      byte[] publicKeyBytes) {
    return verify(algorithm, string2Bytes(str), signBytes, publicKeyBytes);
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, byte[] bytes, String signStr,
      String publicKey) {
    return verify(algorithm, bytes, decode(signStr), publicKey);
  }

  public static boolean verify(RSASignatureAlgorithm algorithm, String str, byte[] signBytes,
      String publicKey) {
    return verify(algorithm, string2Bytes(str), signBytes, publicKey);
  }


  private static <T extends Key & RSAKey> byte[] blockingDoFinal(String transformation, int mode,
      T rsaKey, byte[] bytes, int blockSize) {
    try {
      Cipher cipher = Cipher.getInstance(transformation);
      cipher.init(mode, rsaKey);
      int length = bytes.length;
      if (length <= blockSize) {
        return cipher.doFinal(bytes);
      }
      int offset = 0;
      int remain;
      try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        while (offset < length) {
          remain = length - offset;
          if (remain < blockSize) {
            out.write(cipher.doFinal(bytes, offset, remain));
            offset += blockSize;
          } else {
            out.write(cipher.doFinal(bytes, offset, blockSize));
            offset += blockSize;
          }
        }
        return out.toByteArray();
      }
    } catch (NoSuchAlgorithmException | IOException e) {
      throw new IllegalStateException(e);
    } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
      throw new IllegalArgumentException(e);
    }
  }

}
