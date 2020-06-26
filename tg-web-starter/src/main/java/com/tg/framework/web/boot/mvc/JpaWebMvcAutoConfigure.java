package com.tg.framework.web.boot.mvc;

import com.tg.framework.web.mvc.support.AbstractJpaWebMvcConfigurer;
import javax.persistence.Entity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Entity.class)
@ConditionalOnProperty(prefix = "tg.web.mvc", value = "enabled", matchIfMissing = true)
public class JpaWebMvcAutoConfigure extends AbstractJpaWebMvcConfigurer {

}
