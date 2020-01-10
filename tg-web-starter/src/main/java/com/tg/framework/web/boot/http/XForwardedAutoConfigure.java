package com.tg.framework.web.boot.http;

import com.tg.framework.web.ip.RequestDetailsResolver;
import com.tg.framework.web.ip.support.XForwardedRequestDetailsResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "tg.web.x-forwarded", value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(XForwardedProperties.class)
public class XForwardedAutoConfigure {


  @Bean
  @ConditionalOnMissingBean
  public RequestDetailsResolver requestDetailsResolver(XForwardedProperties xForwardedProperties) {
    return new XForwardedRequestDetailsResolver(xForwardedProperties.getProxyWhitelist(),
        xForwardedProperties.isProxyWhitelistAcceptWildcard(),
        xForwardedProperties.isConsiderNoneAsTrustAll());
  }

}
