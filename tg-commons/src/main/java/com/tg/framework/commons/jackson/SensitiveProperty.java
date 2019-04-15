package com.tg.framework.commons.jackson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveProperty {

  SensitivePropertySerializeStrategy value() default SensitivePropertySerializeStrategy.HIDE;

}
