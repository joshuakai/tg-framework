package com.tg.framework.web.boot.jdbc;

import com.tg.framework.commons.data.jdbc.DynamicDataSource;
import com.tg.framework.commons.data.jdbc.DynamicDataSourceAspect;
import com.tg.framework.commons.data.jdbc.DynamicDataSourceContext;
import com.tg.framework.commons.data.jdbc.UseMaster;
import com.tg.framework.commons.data.jdbc.UseMasterAspect;
import com.tg.framework.commons.data.jdbc.UseSlave;
import com.tg.framework.commons.data.jdbc.UseSlaveAspect;
import com.tg.framework.commons.util.OptionalUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({DataSource.class, HikariConfig.class})
@ConditionalOnProperty(prefix = "tg.datasource", value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceAutoConfigure {

  private static final String USE_MASTER_POINTCUT =
      "@annotation(" + UseMaster.class.getName() + ")";
  private static final String USE_SLAVE_POINTCUT = "@annotation(" + UseSlave.class.getName() + ")";

  @Bean
  public DynamicDataSourceContext dynamicDataSourceContext() {
    return new DynamicDataSourceContext();
  }

  @Bean
  public DataSource dataSource(DynamicDataSourceProperties dynamicDataSourceProperties) {
    return new DynamicDataSource(dynamicDataSourceContext(),
        new HikariDataSource(dynamicDataSourceProperties.getMaster()),
        new HikariDataSource(dynamicDataSourceProperties.getSlave()));
  }

  @Bean
  @ConditionalOnProperty(prefix = "tg.datasource.use-datasource", value = "enabled", matchIfMissing = true)
  public DynamicDataSourceAspect dynamicDataSourceAspect() {
    return new DynamicDataSourceAspect(dynamicDataSourceContext());
  }

  @Bean
  @ConditionalOnProperty(prefix = "tg.datasource.use-master", value = "enabled", matchIfMissing = true)
  public AspectJExpressionPointcutAdvisor useMasterAspect(
      DynamicDataSourceProperties dynamicDataSourceProperties) {
    return new UseMasterAspect(
        OptionalUtils.notEmpty(dynamicDataSourceProperties.getMasterPointcut())
            .map(p -> USE_MASTER_POINTCUT + " or (" + p + ")")
            .orElse(USE_MASTER_POINTCUT),
        dynamicDataSourceContext()
    );
  }

  @Bean
  @ConditionalOnProperty(prefix = "tg.datasource.use-slave", value = "enabled", matchIfMissing = true)
  public AspectJExpressionPointcutAdvisor useSlaveAspect(
      DynamicDataSourceProperties dynamicDataSourceProperties) {
    return new UseSlaveAspect(
        OptionalUtils.notEmpty(dynamicDataSourceProperties.getSlavePointcut())
            .map(p -> USE_SLAVE_POINTCUT + " or (" + p + ")")
            .orElse(USE_SLAVE_POINTCUT),
        dynamicDataSourceContext()
    );
  }

}
