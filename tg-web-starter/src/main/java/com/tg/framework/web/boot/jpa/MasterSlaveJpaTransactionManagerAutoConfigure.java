package com.tg.framework.web.boot.jpa;

import com.tg.framework.data.commons.masterslave.context.MasterOnlyContext;
import com.tg.framework.data.commons.masterslave.datasource.MasterSlaveDataSource;
import com.tg.framework.data.jpa.boot.MasterSlaveJpaTransactionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnBean(MasterSlaveDataSource.class)
@ConditionalOnClass(JpaTransactionManager.class)
public class MasterSlaveJpaTransactionManagerAutoConfigure {

  @Bean
  public PlatformTransactionManager transactionManager(MasterOnlyContext context) {
    return new MasterSlaveJpaTransactionManager(context);
  }

}
