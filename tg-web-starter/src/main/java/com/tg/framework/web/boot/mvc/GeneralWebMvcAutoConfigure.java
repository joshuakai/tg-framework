package com.tg.framework.web.boot.mvc;

import com.tg.framework.web.mvc.support.AbstractGeneralWebMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "tg.web.mvc", value = "enabled", matchIfMissing = true)
public class GeneralWebMvcAutoConfigure extends AbstractGeneralWebMvcConfigurer {

}
