package com.tg.framework.web.boot.data;

import com.tg.framework.commons.data.support.MasterSlaveDataSource;
import com.tg.framework.commons.data.MasterOnly;
import com.tg.framework.commons.data.support.MasterOnlyAspect;
import com.tg.framework.commons.data.MasterOnlyContext;
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

@Configuration
@ConditionalOnClass({DataSource.class, HikariConfig.class})
@ConditionalOnProperty(prefix = "tg.datasource", value = "master-name")
@EnableConfigurationProperties(MasterSlaveDataSourceProperties.class)
public class MasterSlaveDataSourceAutoConfigure {

  private static final String MASTER_ONLY_POINTCUT =
      "@annotation(" + MasterOnly.class.getName() + ")";

  @Bean
  @ConditionalOnMissingBean
  public MasterOnlyContext masterSlaveContext() {
    return new MasterOnlyContext();
  }

  @Bean
  @ConditionalOnMissingBean(MasterSlaveDataSource.class)
  public MasterSlaveDataSource dataSource(
      MasterSlaveDataSourceProperties masterSlaveDataSourceProperties, MasterOnlyContext context) {
    String masterName = masterSlaveDataSourceProperties.getMasterName();
    boolean considerMasterAsSlave = masterSlaveDataSourceProperties.isConsiderMasterAsSlave();
    Map<String, DataSource> dataSourceMap = masterSlaveDataSourceProperties.getDatasourceMap()
        .entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey, e -> new HikariDataSource(e.getValue())));
    return new MasterSlaveDataSource(masterName, considerMasterAsSlave, dataSourceMap, context);
  }

  @Bean
  @ConditionalOnProperty(prefix = "tg.datasource", value = "master-only-pointcut")
  public AspectJExpressionPointcutAdvisor masterOnlyAspect(
      MasterSlaveDataSourceProperties masterSlaveDataSourceProperties, MasterOnlyContext context) {
    return new MasterOnlyAspect(
        OptionalUtils.notEmpty(masterSlaveDataSourceProperties.getMasterOnlyPointcut())
            .map(p -> MASTER_ONLY_POINTCUT + " or (" + p + ")")
            .orElse(MASTER_ONLY_POINTCUT),
        context
    );
  }

}
