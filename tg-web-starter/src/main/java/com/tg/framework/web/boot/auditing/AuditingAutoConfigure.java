package com.tg.framework.web.boot.auditing;

import com.tg.framework.commons.util.SecurityUtils;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;

@Configuration
@EnableJpaAuditing
@ConditionalOnClass(Authentication.class)
@ConditionalOnProperty(prefix = "tg.auditing", value = "enabled", matchIfMissing = true)
public class AuditingAutoConfigure {

  @Bean
  @ConditionalOnMissingBean
  public DateTimeProvider dateTimeProvider() {
    return CurrentDateTimeProvider.INSTANCE;
  }

  @Bean
  @ConditionalOnMissingBean
  public AuditorAware<String> currentAuditorUsernameAware() {
    return new CurrentAuditorUsernameAware();
  }

  static class CurrentAuditorUsernameAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
      return SecurityUtils.getPrincipalAsString();
    }
  }

}
