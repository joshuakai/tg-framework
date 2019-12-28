package com.tg.framework.web.boot.mvc;

import com.tg.framework.web.mvc.AbstractWebMvcSecurityConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnMissingBean(WebMvcConfigurer.class)
@Configuration
@ConfigurationProperties("tg.web")
public class DefaultWebMvcConfig extends AbstractWebMvcSecurityConfiguration {

}
