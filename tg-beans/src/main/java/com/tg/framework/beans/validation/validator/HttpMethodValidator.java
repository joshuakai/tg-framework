package com.tg.framework.beans.validation.validator;

import com.tg.framework.beans.validation.constraint.HttpMethod;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HttpMethodValidator implements ConstraintValidator<HttpMethod, String> {

  private static final Set<String> HTTP_METHODS = Stream.of("GET", "POST", "PUT", "PATCH", "DELETE")
      .collect(Collectors.toSet());

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
      return multiple ? Stream.of(value.split(separator)).allMatch(HTTP_METHODS::contains)
          : HTTP_METHODS.contains(value);
    }
  }

  @Override
  public void initialize(HttpMethod constraintAnnotation) {
    multiple = constraintAnnotation.multiple();
    separator = constraintAnnotation.separator();
    ignoreCase = constraintAnnotation.ignoreCase();
  }
}
