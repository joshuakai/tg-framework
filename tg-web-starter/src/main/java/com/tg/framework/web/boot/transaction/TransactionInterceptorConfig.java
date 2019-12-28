package com.tg.framework.web.boot.transaction;

import java.util.Properties;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
@ConfigurationProperties("tg.transaction.interceptor")
public class TransactionInterceptorConfig {

  private String pointcut;
  private Properties attributes;

  @Bean(name = "transactionInterceptor")
  public TransactionInterceptor transactionInterceptor(
      PlatformTransactionManager platformTransactionManager) {
    return new TransactionInterceptor(platformTransactionManager, attributes);
  }


  @Bean
  public Advisor txAdviceAdvisor(@Qualifier TransactionInterceptor transactionInterceptor) {
    AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
    advisor.setExpression(pointcut);
    advisor.setAdvice(transactionInterceptor);
    return advisor;
  }

  public String getPointcut() {
    return pointcut;
  }

  public void setPointcut(String pointcut) {
    this.pointcut = pointcut;
  }

  public Properties getAttributes() {
    return attributes;
  }

  public void setAttributes(Properties attributes) {
    this.attributes = attributes;
  }
}
