package com.tg.framework.web.boot.jdbc;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tg.datasource")
public class DynamicDataSourceProperties {

  private String masterPointcut;
  private String slavePointcut;
  private HikariConfig master;
  private HikariConfig slave;

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
