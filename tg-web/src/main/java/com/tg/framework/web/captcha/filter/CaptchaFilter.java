package com.tg.framework.web.captcha.filter;

import com.tg.framework.web.captcha.CaptchaArgumentResolver;
import com.tg.framework.web.captcha.CaptchaFailureHandler;
import com.tg.framework.web.captcha.CaptchaProvider;
import com.tg.framework.web.captcha.support.DefaultCaptchaFailureHandler;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

public class CaptchaFilter extends OncePerRequestFilter {

  private CaptchaArgumentResolver captchaArgumentResolver;
  private CaptchaProvider captchaProvider;
  private CaptchaFailureHandler captchaFailureHandler = new DefaultCaptchaFailureHandler();


  public CaptchaFilter(CaptchaArgumentResolver captchaArgumentResolver,
      CaptchaProvider captchaProvider) {
    Assert.notNull(captchaArgumentResolver, "CaptchaArgumentResolver must not be null.");
    Assert.notNull(captchaProvider, "CaptchaProvider must not be null.");
    this.captchaArgumentResolver = captchaArgumentResolver;
    this.captchaProvider = captchaProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (!captchaProvider.provide(request)
        .validate(captchaArgumentResolver.resolveArgument(request))) {
      captchaFailureHandler.onCaptchaFailure(request, response, null);
    }
    filterChain.doFilter(request, response);
  }

  public void setCaptchaFailureHandler(CaptchaFailureHandler captchaFailureHandler) {
    Assert.notNull(captchaFailureHandler, "CaptchaFailureHandler must not be null.");
    this.captchaFailureHandler = captchaFailureHandler;
  }

}