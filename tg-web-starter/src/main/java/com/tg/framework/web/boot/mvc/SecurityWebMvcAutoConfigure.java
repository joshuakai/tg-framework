package com.tg.framework.web.boot.mvc;

import com.tg.framework.web.mvc.support.AbstractSecurityWebMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

@Configuration
@ConditionalOnClass(Authentication.class)
@ConditionalOnProperty(prefix = "tg.web.mvc", value = "enabled", matchIfMissing = true)
public class SecurityWebMvcAutoConfigure extends AbstractSecurityWebMvcConfigurer {

}
