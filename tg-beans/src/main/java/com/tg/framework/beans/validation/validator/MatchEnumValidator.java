package com.tg.framework.beans.validation.validator;

import com.tg.framework.beans.validation.constraint.MatchEnum;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchEnumValidator implements ConstraintValidator<MatchEnum, Object> {

  private Class<?> enumClass;
  private Method valueAccessor;
  private boolean multiple;
  private String separator;
  private boolean ignoreCase;

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null || (value instanceof String && ((String) value).trim().length() == 0)) {
      return true;
    } else if (value instanceof String && multiple) {
      String str = (String) value;
      return Stream.of(str.split(separator)).allMatch(
          v -> Stream.of(enumClass.getEnumConstants()).map(this::accessEnumValue)
              .anyMatch(ev -> equals(v, ev, ignoreCase)));
    } else {
      return Stream.of(enumClass.getEnumConstants()).map(this::accessEnumValue)
          .anyMatch(ev -> equals(value, ev, ignoreCase));
    }
  }

  @Override
  public void initialize(MatchEnum constraintAnnotation) {
    enumClass = constraintAnnotation.enumClass();
    try {
      valueAccessor = enumClass.getDeclaredMethod(constraintAnnotation.valueAccessor());
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Invalid valueAccessor.", e);
    }
    multiple = constraintAnnotation.multiple();
    separator = constraintAnnotation.separator();
    ignoreCase = constraintAnnotation.ignoreCase();
  }

  private Object accessEnumValue(Object object) {
    try {
      return valueAccessor.invoke(object);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid valueAccessor.");
    }
  }

  private static boolean equals(Object value, Object enumValue, boolean ignoreCase) {
    boolean isString = String.class.isAssignableFrom(enumValue.getClass());
    if (isString || value.getClass() != enumValue.getClass()) {
      return equals(value.toString(), enumValue.toString(), ignoreCase);
    }
    return value.equals(enumValue);
  }

  private static boolean equals(String value, String enumValue, boolean ignoreCase) {
    return ignoreCase ? value.equalsIgnoreCase(enumValue) : value.equals(enumValue);
  }

}
