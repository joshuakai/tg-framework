package com.tg.framework.commons.validation.validator;

import com.tg.framework.commons.util.ReflectionUtils;
import com.tg.framework.commons.validation.Matchable;
import com.tg.framework.commons.validation.constraint.MatchEnum;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class MatchEnumValidator implements ConstraintValidator<MatchEnum, Object> {

  private Class<? extends Matchable> enumClass;
  private boolean isInteger;
  private boolean multiple;
  private String separator;

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null || (value instanceof String && StringUtils.isEmpty(value.toString()))) {
      return true;
    } else if (value instanceof String && multiple) {
      String str = (String) value;
      if (isInteger) {
        return Stream.of(StringUtils.split(str, separator))
            .map(s -> Integer.parseInt(s))
            .allMatch(v -> Stream.of(enumClass.getEnumConstants()).anyMatch(e -> e.matches(v)));
      }
      return Stream.of(StringUtils.split(str, separator))
          .allMatch(v -> Stream.of(enumClass.getEnumConstants()).anyMatch(e -> e.matches(v)));
    } else {
      return Stream.of(enumClass.getEnumConstants()).anyMatch(e -> e.matches(value));
    }
  }

  @Override
  public void initialize(MatchEnum constraintAnnotation) {
    enumClass = constraintAnnotation.enumClass();
    isInteger = ReflectionUtils.getGenericType(enumClass)
        .filter(t -> t.isAssignableFrom(Integer.class)).isPresent();
    multiple = constraintAnnotation.multiple();
    separator = constraintAnnotation.separator();
  }

}
