package com.tg.framework.commons.jackson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mask {

  MaskType value() default MaskType.CENTER;

  int minMaskLength() default 3;

  int maxLeftLength() default 3;

  int maxRightLength() default 3;

  char symbol() default '*';

  int symbolLength() default 3;

  String defaultValue() default "";

}
