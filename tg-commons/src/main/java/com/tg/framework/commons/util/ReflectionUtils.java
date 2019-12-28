package com.tg.framework.commons.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public class ReflectionUtils {

  private ReflectionUtils() {
  }

  public static Optional<Method> getMethod(Class<?> clazz, String name,
      Class<?>... parameterTypes) {
    Method method = null;
    try {
      method = clazz.getDeclaredMethod(name, parameterTypes);
    } catch (NoSuchMethodException e) {
      if (clazz != Object.class && clazz.getSuperclass() != null) {
        return getMethod(clazz.getSuperclass(), name);
      }
    }
    return Optional.ofNullable(method);
  }

  public static <T> Optional<Class<T>> getGenericType(Class<?> clazz, int position) {
    return Optional.ofNullable(clazz)
        .map(c -> (ParameterizedType) clazz.getGenericSuperclass())
        .map(ParameterizedType::getActualTypeArguments)
        .filter(ts -> ts.length > position)
        .map(ts -> (Class<T>) ts[position]);
  }

  public static <T> Optional<Class<T>> getGenericType(Class<?> clazz) {
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
        if (constructor == null && useDefaultConstructor) {
          throw clazz.newInstance();
        }
        throw constructor.newInstance(args);
      }
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

}
