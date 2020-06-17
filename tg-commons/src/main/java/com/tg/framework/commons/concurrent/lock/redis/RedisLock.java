package com.tg.framework.commons.concurrent.lock.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

  boolean useExpression() default true;

  String key();

  boolean mutex() default false;

  long timeout() default -1L;

  TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

  long sleepMillis() default 100L;

  long releaseDelay() default 0L;

  String message() default "Concurrent.Lock.RedisLockFailed";

}
