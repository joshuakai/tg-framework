package com.tg.framework.commons.validation.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.tg.framework.commons.validation.Matchable;
import com.tg.framework.commons.validation.validator.MatchEnumValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MatchEnumValidator.class)
public @interface MatchEnum {

  String message() default "{com.tg.framework.commons.validation.constraint.MatchEnum.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<? extends Matchable> enumClass();

  boolean multiple() default false;

  String separator() default ",";

}
