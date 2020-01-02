package com.tg.framework.web.boot.mvc;

import com.tg.framework.web.mvc.AbstractWebMvcSecurityConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnMissingBean(WebMvcConfigurer.class)
@Configuration
public class DefaultWebMvcConfig extends AbstractWebMvcSecurityConfiguration {

}
