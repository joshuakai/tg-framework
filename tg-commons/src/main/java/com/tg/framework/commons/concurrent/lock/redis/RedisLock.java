package com.tg.framework.commons.concurrent.lock.redis;

import com.tg.framework.commons.concurrent.lock.exception.LockMutexException;
import com.tg.framework.commons.concurrent.lock.exception.LockTimeoutException;
import com.tg.framework.commons.concurrent.lock.LockTimeoutStrategy;
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

  Class<? extends LockMutexException> mutexException() default LockMutexException.class;

  long timeout() default -1L;

  TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

  LockTimeoutStrategy timeoutStrategy() default LockTimeoutStrategy.THROW_EXCEPTION_WHILE_TIMEOUT;

  Class<? extends LockTimeoutException> timeoutException() default LockTimeoutException.class;

  long sleepMillis() default 100L;

}
