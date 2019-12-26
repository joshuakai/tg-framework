package com.tg.framework.web.boot.mvc.resolver.annotation;

import com.tg.framework.web.http.support.IpResolverStrategy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestIp {

  IpResolverStrategy strategy() default IpResolverStrategy.AUTO;

  String customHeaderName() default "";

}
