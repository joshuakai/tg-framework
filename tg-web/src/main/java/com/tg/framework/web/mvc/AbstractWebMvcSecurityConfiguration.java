package com.tg.framework.web.mvc;

import com.tg.framework.web.mvc.handler.DefaultExceptionHandler;
import com.tg.framework.web.mvc.handler.SecurityExceptionHandler;
import com.tg.framework.web.mvc.resolver.PrincipalHandlerMethodArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

public class AbstractWebMvcSecurityConfiguration extends AbstractWebMvcConfiguration {

  @Bean
  public DefaultExceptionHandler defaultExceptionHandler() {
    return new SecurityExceptionHandler();
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    super.addArgumentResolvers(argumentResolvers);
    argumentResolvers.add(new PrincipalHandlerMethodArgumentResolver());
  }
}
