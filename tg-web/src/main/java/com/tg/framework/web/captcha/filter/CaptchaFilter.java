package com.tg.framework.web.captcha.filter;

import com.tg.framework.web.captcha.CaptchaArgumentResolver;
import com.tg.framework.web.captcha.CaptchaFailureHandler;
import com.tg.framework.web.captcha.CaptchaProvider;
import com.tg.framework.web.util.HttpUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

public class CaptchaFilter extends OncePerRequestFilter {

  private static Logger logger = LoggerFactory.getLogger(CaptchaFilter.class);

  private CaptchaArgumentResolver captchaArgumentResolver;
  private CaptchaProvider captchaProvider;
  private CaptchaFailureHandler captchaFailureHandler = CaptchaFailureHandler.DEFAULT;


  public CaptchaFilter(CaptchaArgumentResolver captchaArgumentResolver,
      CaptchaProvider captchaProvider) {
    Assert.notNull(captchaArgumentResolver, "A captcha argument resolver must be set");
    Assert.notNull(captchaProvider, "A captcha provider must be set");
    this.captchaArgumentResolver = captchaArgumentResolver;
    this.captchaProvider = captchaProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String captcha = captchaArgumentResolver.resolveArgument(request);
    if (!captchaProvider.provide(request).vote(captcha)) {
      logger.warn("Bad captcha: {} {}.", captcha, HttpUtils.getUrl(request));
      if (captchaFailureHandler.onCaptchaFailure(request, response, null)) {
        filterChain.doFilter(request, response);
      }
    }
  }

  public void setCaptchaFailureHandler(CaptchaFailureHandler captchaFailureHandler) {
    Assert.notNull(captchaFailureHandler, "A captcha failure handler must be set");
    this.captchaFailureHandler = captchaFailureHandler;
  }

}
