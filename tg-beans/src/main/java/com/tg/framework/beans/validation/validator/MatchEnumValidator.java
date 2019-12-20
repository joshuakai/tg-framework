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

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null || (value instanceof String && ((String) value).trim().length() == 0)) {
      return true;
    } else if (value instanceof String && multiple) {
      String str = (String) value;
      return Stream.of(str.split(separator)).allMatch(
          v -> Stream.of(enumClass.getEnumConstants()).map(this::accessEnumValue)
              .anyMatch(ev -> equals(v, ev)));
    } else {
      return Stream.of(enumClass.getEnumConstants()).map(this::accessEnumValue)
          .anyMatch(ev -> equals(value, ev));
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
  }

  private Object accessEnumValue(Object object) {
    try {
      return valueAccessor.invoke(object);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid valueAccessor.");
    }
  }

  private static boolean equals(Object value, Object enumValue) {
    if (value.getClass() == enumValue.getClass()) {
      return value.equals(enumValue);
    }
    return value.toString().equals(enumValue.toString());
  }

}
