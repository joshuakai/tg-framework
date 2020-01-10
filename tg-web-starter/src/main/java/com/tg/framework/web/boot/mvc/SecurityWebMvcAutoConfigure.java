package com.tg.framework.web.boot.mvc;

import com.tg.framework.web.mvc.AbstractWebMvcConfiguration;
import com.tg.framework.web.mvc.handler.DefaultExceptionHandler;
import com.tg.framework.web.mvc.handler.SecurityExceptionHandler;
import com.tg.framework.web.mvc.resolver.PrincipalHandlerMethodArgumentResolver;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnMissingBean(WebMvcConfigurer.class)
@ConditionalOnClass(Authentication.class)
@ConditionalOnProperty(prefix = "tg.web.mvc", value = "enabled", matchIfMissing = true)
public class SecurityWebMvcAutoConfigure extends AbstractWebMvcConfiguration {

  @Bean
  @Override
  public DefaultExceptionHandler defaultExceptionHandler() {
    return new SecurityExceptionHandler(requestDetailsResolver);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    super.addArgumentResolvers(argumentResolvers);
    argumentResolvers.add(new PrincipalHandlerMethodArgumentResolver());
  }

}
