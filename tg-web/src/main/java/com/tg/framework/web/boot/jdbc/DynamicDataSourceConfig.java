package com.tg.framework.web.boot.jdbc;

import com.tg.framework.commons.lang.StringOptional;
import com.tg.framework.core.data.jdbc.DynamicDataSource;
import com.tg.framework.core.data.jdbc.DynamicDataSourceAspect;
import com.tg.framework.core.data.jdbc.UseMaster;
import com.tg.framework.core.data.jdbc.UseMasterAspect;
import com.tg.framework.core.data.jdbc.UseSlave;
import com.tg.framework.core.data.jdbc.UseSlaveAspect;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConfigurationProperties("tg.datasource")
public class DynamicDataSourceConfig {

  private static final String USE_MASTER_POINTCUT =
      "@annotation(" + UseMaster.class.getName() + ")";
  private static final String USE_SLAVE_POINTCUT = "@annotation(" + UseSlave.class.getName() + ")";

  private String masterPointcut;
  private String slavePointcut;
  private HikariConfig master;
  private HikariConfig slave;

  @Bean
  @Primary
  public DataSource dataSource() {
    return new DynamicDataSource(new HikariDataSource(master), new HikariDataSource(slave));
  }

  @Bean
  public DynamicDataSourceAspect dynamicDataSourceAspect() {
    return new DynamicDataSourceAspect();
  }

  @Bean
  public AspectJExpressionPointcutAdvisor useMasterAspect() {
    return new UseMasterAspect(
        StringOptional.ofNullable(masterPointcut).map(p -> USE_MASTER_POINTCUT + " or (" + p + ")")
            .orElse(USE_MASTER_POINTCUT));
  }

  @Bean
  public AspectJExpressionPointcutAdvisor useSlaveAspect() {
    return new UseSlaveAspect(
        StringOptional.ofNullable(slavePointcut).map(p -> USE_SLAVE_POINTCUT + " or (" + p + ")")
            .orElse(USE_SLAVE_POINTCUT));
  }

  public String getMasterPointcut() {
    return masterPointcut;
  }

  public void setMasterPointcut(String masterPointcut) {
    this.masterPointcut = masterPointcut;
  }

  public String getSlavePointcut() {
    return slavePointcut;
  }

  public void setSlavePointcut(String slavePointcut) {
    this.slavePointcut = slavePointcut;
  }

  public HikariConfig getMaster() {
    return master;
  }

  public void setMaster(HikariConfig master) {
    this.master = master;
  }

  public HikariConfig getSlave() {
    return slave;
  }

  public void setSlave(HikariConfig slave) {
    this.slave = slave;
  }
}
