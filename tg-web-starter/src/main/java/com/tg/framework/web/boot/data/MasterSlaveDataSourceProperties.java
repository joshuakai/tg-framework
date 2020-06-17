package com.tg.framework.web.boot.data;

import com.zaxxer.hikari.HikariConfig;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tg.datasource")
public class MasterSlaveDataSourceProperties {

  private String masterName;
  private boolean considerMasterAsSlave;
  private Map<String, HikariConfig> datasourceMap;
  private String masterOnlyPointcut;

  public String getMasterName() {
    return masterName;
  }

  public void setMasterName(String masterName) {
    this.masterName = masterName;
  }

  public boolean isConsiderMasterAsSlave() {
    return considerMasterAsSlave;
  }

  public void setConsiderMasterAsSlave(boolean considerMasterAsSlave) {
    this.considerMasterAsSlave = considerMasterAsSlave;
  }

  public Map<String, HikariConfig> getDatasourceMap() {
    return datasourceMap;
  }

  public void setDatasourceMap(
      Map<String, HikariConfig> datasourceMap) {
    this.datasourceMap = datasourceMap;
  }

  public String getMasterOnlyPointcut() {
    return masterOnlyPointcut;
  }

  public void setMasterOnlyPointcut(String masterOnlyPointcut) {
    this.masterOnlyPointcut = masterOnlyPointcut;
  }
}
