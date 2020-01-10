package com.tg.framework.commons.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class ReflectionUtils {

  private ReflectionUtils() {
  }

  public static Method getMethod(Class<?> clazz, String name,
      Class<?>... parameterTypes) {
    Method method = null;
    try {
      method = clazz.getDeclaredMethod(name, parameterTypes);
    } catch (NoSuchMethodException e) {
      if (clazz != Object.class && clazz.getSuperclass() != null) {
        return getMethod(clazz.getSuperclass(), name);
      }
    }
    return method;
  }

  @SuppressWarnings("unchecked")
  public static <T> Class<T> getGenericType(Class<?> clazz, int position) {
    if (clazz != null && position >= 0) {
      Type[] types = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
      if (types != null && types.length > position) {
        return (Class<T>) types[position];
      }
    }
    return null;
  }

  public static <T> Class<T> getGenericType(Class<?> clazz) {
    return getGenericType(clazz, 0);
  }

  public static <T extends Throwable> void throwException(Class<T> clazz,
      boolean useDefaultConstructor, Object... args) throws T {
    try {
      if (args == null) {
        throw clazz.newInstance();
      } else {
        Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
          argTypes[i] = args[i].getClass();
        }
        Constructor<T> constructor = clazz.getConstructor(argTypes);
        if (constructor != null) {
          throw constructor.newInstance(args);
        } else if (useDefaultConstructor) {
          throw clazz.newInstance();
        }
        throw new IllegalAccessException();
      }
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

}
