package com.tg.framework.commons.concurrent.lock.scheduling;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledCluster {

  boolean useExpression() default false;

  String key();

  long releaseDelay() default 0L;

}
