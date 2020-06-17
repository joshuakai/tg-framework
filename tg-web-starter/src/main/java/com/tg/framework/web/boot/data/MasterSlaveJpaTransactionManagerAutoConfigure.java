package com.tg.framework.web.boot.data;

import com.tg.framework.commons.data.support.MasterSlaveDataSource;
import com.tg.framework.commons.data.MasterOnlyContext;
import com.tg.framework.data.jpa.MasterSlaveJpaTransactionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
@ConditionalOnClass(JpaTransactionManager.class)
@ConditionalOnBean({MasterOnlyContext.class, MasterSlaveDataSource.class})
public class MasterSlaveJpaTransactionManagerAutoConfigure {

  @Bean
  @ConditionalOnMissingBean
  public MasterSlaveJpaTransactionManager transactionManager(MasterOnlyContext masterOnlyContext) {
    return new MasterSlaveJpaTransactionManager(masterOnlyContext);
  }

}
