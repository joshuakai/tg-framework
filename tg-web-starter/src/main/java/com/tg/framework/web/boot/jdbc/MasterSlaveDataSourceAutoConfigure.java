package com.tg.framework.web.boot.jdbc;

import com.tg.framework.commons.data.jdbc.MasterOnly;
import com.tg.framework.commons.data.jdbc.MasterOnlyAspect;
import com.tg.framework.commons.data.jdbc.MasterOnlyContext;
import com.tg.framework.commons.data.jdbc.MasterSlaveDataSource;
import com.tg.framework.commons.data.jdbc.MasterSlaveJpaTransactionManager;
import com.tg.framework.commons.util.OptionalUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnClass({DataSource.class, HikariConfig.class})
@ConditionalOnProperty(prefix = "tg.datasource", value = "enabled", matchIfMissing = true)
@ConditionalOnMissingBean(DataSource.class)
@EnableConfigurationProperties(MasterSlaveDataSourceProperties.class)
public class MasterSlaveDataSourceAutoConfigure {

  private static final String MASTER_ONLY_POINTCUT =
      "@annotation(" + MasterOnly.class.getName() + ")";

  @Bean
  public MasterOnlyContext masterSlaveContext() {
    return new MasterOnlyContext();
  }

  @Bean
  public DataSource dataSource(MasterSlaveDataSourceProperties masterSlaveDataSourceProperties,
      MasterOnlyContext context) {
    String masterName = masterSlaveDataSourceProperties.getMasterName();
    boolean considerMasterAsSlave = masterSlaveDataSourceProperties.isConsiderMasterAsSlave();
    Map<String, DataSource> dataSourceMap = masterSlaveDataSourceProperties.getDatasourceMap()
        .entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey, e -> new HikariDataSource(e.getValue())));
    return new MasterSlaveDataSource(masterName, considerMasterAsSlave, dataSourceMap, context);
  }

  @Bean
  @ConditionalOnProperty(prefix = "tg.datasource.master-only", value = "enabled", matchIfMissing = true)
  public AspectJExpressionPointcutAdvisor masterOnlyAspect(
      MasterSlaveDataSourceProperties masterSlaveDataSourceProperties, MasterOnlyContext context) {
    return new MasterOnlyAspect(
        OptionalUtils.notEmpty(masterSlaveDataSourceProperties.getMasterOnlyPointcut())
            .map(p -> MASTER_ONLY_POINTCUT + " or (" + p + ")")
            .orElse(MASTER_ONLY_POINTCUT),
        context
    );
  }

  @Bean
  public PlatformTransactionManager transactionManager(MasterOnlyContext context) {
    return new MasterSlaveJpaTransactionManager(context);
  }

}
