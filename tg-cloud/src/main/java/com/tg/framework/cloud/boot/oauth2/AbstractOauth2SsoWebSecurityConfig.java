package com.tg.framework.cloud.boot.oauth2;

import com.tg.framework.web.filter.CsrfHeaderFilter;
import com.tg.framework.web.util.WebSecurityUtils;
import javax.annotation.Resource;
import javax.servlet.Filter;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AbstractOauth2SsoWebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Resource
  protected WebEndpointProperties webEndpointProperties;

  protected String csrfCookieName() {
    return "X-XSRF-TOKEN";
  }

  protected String csrfCookiePath() {
    return "/";
  }

  protected Filter csrfHeaderFilter() {
    return new CsrfHeaderFilter(csrfCookieName(), csrfCookiePath());
  }

  protected CsrfTokenRepository csrfTokenRepository() {
    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
    repository.setHeaderName(csrfCookieName());
    return repository;
  }

  protected RequestMatcher[] ignoringRequestMatchers() throws Exception {
    return new RequestMatcher[]{new AntPathRequestMatcher(
        WebSecurityUtils.antPatternOfBasePath(webEndpointProperties.getBasePath()),
        HttpMethod.GET.toString())};
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    RequestMatcher[] ignoringRequestMatchers = ignoringRequestMatchers();
    http.authorizeRequests().requestMatchers(ignoringRequestMatchers).permitAll().anyRequest()
        .authenticated().and().csrf().ignoringRequestMatchers(ignoringRequestMatchers)
        .csrfTokenRepository(csrfTokenRepository()).and()
        .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
  }

  @Bean
  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }
}
