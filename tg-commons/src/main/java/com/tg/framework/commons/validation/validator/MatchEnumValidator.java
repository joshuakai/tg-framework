package com.tg.framework.commons.validation.validator;

import com.tg.framework.commons.util.ReflectionUtils;
import com.tg.framework.commons.validation.constraint.MatchEnum;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class MatchEnumValidator implements ConstraintValidator<MatchEnum, Object> {

  private Class<?> enumClass;
  private Method valueAccessor;
  private boolean multiple;
  private String separator;

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null || (value instanceof String && StringUtils.isEmpty(value.toString()))) {
      return true;
    } else if (value instanceof String && multiple) {
      String str = (String) value;
      return Stream.of(StringUtils.split(str, separator)).allMatch(
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
    valueAccessor = ReflectionUtils.getMethod(enumClass, constraintAnnotation.valueAccessor())
        .orElseThrow(() -> new IllegalArgumentException("Invalid valueAccessor."));
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
    return StringUtils.equals(value.toString(), enumValue.toString());
  }

}
