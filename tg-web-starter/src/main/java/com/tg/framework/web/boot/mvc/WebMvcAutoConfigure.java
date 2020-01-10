package com.tg.framework.web.boot.mvc;

import com.tg.framework.web.mvc.AbstractWebMvcConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(AbstractWebMvcConfiguration.class)
@ConditionalOnMissingClass("org.springframework.security.core.Authentication")
@ConditionalOnProperty(prefix = "tg.web.mvc", value = "enabled", matchIfMissing = true)
public class WebMvcAutoConfigure extends AbstractWebMvcConfiguration {

}
