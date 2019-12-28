package com.tg.framework.beans.validation.validator;

import com.tg.framework.beans.validation.constraint.Charset;
import java.util.Set;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CharsetValidator implements ConstraintValidator<Charset, String> {

  private static final Set<String> CHARSETS = java.nio.charset.Charset.availableCharsets().keySet();

  private boolean multiple;
  private String separator;
  private boolean ignoreCase;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.trim().length() == 0) {
      return true;
    } else {
      if (ignoreCase) {
        value = value.toUpperCase();
      }
      return multiple ? Stream.of(value.split(separator)).allMatch(CHARSETS::contains)
          : CHARSETS.contains(value);
    }
  }

  @Override
  public void initialize(Charset constraintAnnotation) {
    multiple = constraintAnnotation.multiple();
    separator = constraintAnnotation.separator();
    ignoreCase = constraintAnnotation.ignoreCase();
  }

}
