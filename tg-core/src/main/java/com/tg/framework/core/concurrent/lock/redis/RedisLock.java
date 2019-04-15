package com.tg.framework.core.concurrent.lock.redis;

import com.tg.framework.core.concurrent.lock.LockTimeoutException;
import com.tg.framework.core.concurrent.lock.LockTimeoutStrategy;
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

  long timeout() default -1L;

  TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

  LockTimeoutStrategy strategy() default LockTimeoutStrategy.THROW_EXCEPTION_WHILE_TIMEOUT;

  Class<? extends Throwable> exceptionClass() default LockTimeoutException.class;

  long sleepMillis() default 100L;

}
