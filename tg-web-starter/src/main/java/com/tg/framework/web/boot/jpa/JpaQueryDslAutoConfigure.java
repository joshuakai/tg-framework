package com.tg.framework.web.boot.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(JPAQueryFactory.class)
public class JpaQueryDslAutoConfigure {

  @Bean
  public JPAQueryFactory queryFactory(EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }

}
