package com.tg.framework.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationUtils {

  private SerializationUtils() {
  }

  public static byte[] serialize(Object object) throws IOException {
    try (
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bao)
    ) {
      oo.writeObject(object);
      return bao.toByteArray();
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
    try (
        ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(bai)
    ) {
      return (T) oi.readObject();
    }
  }

}
