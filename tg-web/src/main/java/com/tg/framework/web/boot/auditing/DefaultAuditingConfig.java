package com.tg.framework.web.boot.auditing;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@EnableJpaAuditing
public class DefaultAuditingConfig {

  @Bean
  public DateTimeProvider dateTimeProvider() {
    return CurrentDateTimeProvider.INSTANCE;
  }

  @Bean
  public AuditorAware<String> currentAuditorUsernameAware() {
    return new CurrentAuditorUsernameAware();
  }

  static class CurrentAuditorUsernameAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
      return Optional.ofNullable(SecurityContextHolder.getContext())
          .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).map(p -> {
            if (p instanceof UserDetails) {
              return ((UserDetails) p).getUsername();
            }
            return p.toString();
          });
    }
  }

}
