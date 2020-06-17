package com.tg.framework.commons.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;

public class DIYAssert {

  public static <E extends Throwable> void isTrue(boolean expression, Supplier<E> errorSupplier)
      throws E {
    if (!expression) {
      throw errorSupplier.get();
    }
  }

  public static <E extends Throwable> void isFalse(boolean expression, Supplier<E> errorSupplier)
      throws E {
    isTrue(!expression, errorSupplier);
  }

  public static <E extends Throwable> void isNull(Object object, Supplier<E> errorSupplier)
      throws E {
    isTrue(object == null, errorSupplier);
  }

  public static <E extends Throwable> void notNull(Object object, Supplier<E> errorSupplier)
      throws E {
    isTrue(object != null, errorSupplier);
  }

  public static <E extends Throwable> void equals(Object object, Object anotherObject,
      Supplier<E> errorSupplier)
      throws E {
    isTrue(Objects.equals(object, anotherObject), errorSupplier);
  }

  public static <E extends Throwable> void notEqual(Object object, Object anotherObject,
      Supplier<E> errorSupplier) throws E {
    isTrue(!Objects.equals(object, anotherObject), errorSupplier);
  }

  public static <E extends Throwable> void isEmpty(String text, Supplier<E> errorSupplier)
      throws E {
    isTrue(StringUtils.isEmpty(text), errorSupplier);
  }

  public static <E extends Throwable> void notEmpty(String text, Supplier<E> errorSupplier)
      throws E {
    isTrue(StringUtils.isNotEmpty(text), errorSupplier);
  }

  public static <E extends Throwable> void notBlank(String text, Supplier<E> errorSupplier)
      throws E {
    isTrue(StringUtils.isNotBlank(text), errorSupplier);
  }

  public static <E extends Throwable> void isBlank(String text, Supplier<E> errorSupplier)
      throws E {
    isTrue(StringUtils.isBlank(text), errorSupplier);
  }

  public static <E extends Throwable> void equalsIgnoreCase(String text, String anotherText,
      Supplier<E> errorSupplier)
      throws E {
    isTrue(StringUtils.equalsIgnoreCase(text, anotherText), errorSupplier);
  }

  public static <E extends Throwable> void startsWith(String text, String prefix,
      Supplier<E> errorSupplier) throws E {
    isTrue(StringUtils.startsWith(text, prefix), errorSupplier);
  }

  public static <E extends Throwable> void startsWithIgnoreCase(String text, String prefix,
      Supplier<E> errorSupplier) throws E {
    isTrue(StringUtils.startsWithIgnoreCase(text, prefix), errorSupplier);
  }

  public static <E extends Throwable> void endsWith(String text, String suffix,
      Supplier<E> errorSupplier) throws E {
    isTrue(StringUtils.endsWith(text, suffix), errorSupplier);
  }

  public static <E extends Throwable> void endsWithIgnoreCase(String text, String suffix,
      Supplier<E> errorSupplier) throws E {
    isTrue(StringUtils.endsWithIgnoreCase(text, suffix), errorSupplier);
  }

  public static <E extends Throwable> void contains(String text, String searchText,
      Supplier<E> errorSupplier) throws E {
    isTrue(StringUtils.contains(text, searchText), errorSupplier);
  }

  public static <E extends Throwable> void containsIgnoreCase(String text, String searchText,
      Supplier<E> errorSupplier) throws E {
    isTrue(StringUtils.containsIgnoreCase(text, searchText), errorSupplier);
  }

  public static <E extends Throwable> void isEmpty(Object[] array, Supplier<E> errorSupplier)
      throws E {
    isTrue(array == null || array.length == 0, errorSupplier);
  }

  public static <E extends Throwable> void notEmpty(Object[] array, Supplier<E> errorSupplier)
      throws E {
    isTrue(array != null && array.length != 0, errorSupplier);
  }

  public static <E extends Throwable> void noneNull(Object[] array, Supplier<E> errorSupplier)
      throws E {
    if (array != null) {
      for (Object element : array) {
        notNull(element, errorSupplier);
      }
    }
  }

  public static <E extends Throwable> void noneEmpty(String[] array, Supplier<E> errorSupplier)
      throws E {
    if (array != null) {
      for (String element : array) {
        notEmpty(element, errorSupplier);
      }
    }
  }

  public static <E extends Throwable> void noneBlank(String[] array, Supplier<E> errorSupplier)
      throws E {
    if (array != null) {
      for (String element : array) {
        notBlank(element, errorSupplier);
      }
    }
  }

  public static <E extends Throwable> void isEmpty(Collection<?> collection,
      Supplier<E> errorSupplier) throws E {
    isTrue(collection == null || collection.isEmpty(), errorSupplier);
  }

  public static <E extends Throwable> void notEmpty(Collection<?> collection,
      Supplier<E> errorSupplier) throws E {
    isTrue(collection != null && !collection.isEmpty(), errorSupplier);
  }

  public static <E extends Throwable> void noneNull(Collection<?> collection,
      Supplier<E> errorSupplier) throws E {
    if (collection != null) {
      for (Object element : collection) {
        notNull(element, errorSupplier);
      }
    }
  }

  public static <E extends Throwable> void noneEmpty(Collection<String> collection,
      Supplier<E> errorSupplier) throws E {
    if (collection != null) {
      for (String element : collection) {
        notEmpty(element, errorSupplier);
      }
    }
  }

  public static <E extends Throwable> void noneBlank(Collection<String> collection,
      Supplier<E> errorSupplier) throws E {
    if (collection != null) {
      for (String element : collection) {
        notBlank(element, errorSupplier);
      }
    }
  }

  public static <E extends Throwable> void isEmpty(Map<?, ?> map, Supplier<E> errorSupplier)
      throws E {
    isTrue(map == null || map.isEmpty(), errorSupplier);
  }

  public static <E extends Throwable> void notEmpty(Map<?, ?> map, Supplier<E> errorSupplier)
      throws E {
    isTrue(map != null && map.size() != 0, errorSupplier);
  }

  public static <E extends Throwable> void isInstanceOf(Class<?> clazz, Object object,
      Supplier<E> errorSupplier) throws E {
    isTrue(clazz.isInstance(object), errorSupplier);
  }

  public static <E extends Throwable> void isAssignableFrom(Class<?> superClazz, Class<?> subClass,
      Supplier<E> errorSupplier) throws E {
    isTrue(superClazz.isAssignableFrom(subClass), errorSupplier);
  }

}
