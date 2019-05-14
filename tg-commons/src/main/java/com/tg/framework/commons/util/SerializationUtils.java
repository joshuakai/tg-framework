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
    ByteArrayOutputStream bao = new ByteArrayOutputStream();
    new ObjectOutputStream(bao).writeObject(object);
    return bao.toByteArray();
  }

  public static <T> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
    return (T) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
  }

}
