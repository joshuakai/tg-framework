package com.tg.framework.web.boot.data;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
@ConditionalOnProperty(prefix = "tg.transaction.interceptor", value = "enabled")
@EnableConfigurationProperties(TransactionInterceptorProperties.class)
public class TransactionInterceptorAutoConfigure {

  @Bean(name = "transactionInterceptor")
  public TransactionInterceptor transactionInterceptor(
      PlatformTransactionManager platformTransactionManager,
      TransactionInterceptorProperties transactionInterceptorProperties) {
    return new TransactionInterceptor(platformTransactionManager,
        transactionInterceptorProperties.getAttributes());
  }

  @Bean
  public Advisor txAdviceAdvisor(TransactionInterceptorProperties transactionInterceptorProperties,
      @Qualifier TransactionInterceptor transactionInterceptor) {
    AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
    advisor.setExpression(transactionInterceptorProperties.getPointcut());
    advisor.setAdvice(transactionInterceptor);
    return advisor;
  }

}
