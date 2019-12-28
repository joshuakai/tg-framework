package com.tg.framework.beans.validation.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.tg.framework.beans.validation.validator.HttpMethodValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = HttpMethodValidator.class)
public @interface HttpMethod {

  String message() default "{com.tg.framework.beans.validation.constraint.HttpMethod.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean multiple() default false;

  String separator() default ",";

  boolean ignoreCase() default true;

}
