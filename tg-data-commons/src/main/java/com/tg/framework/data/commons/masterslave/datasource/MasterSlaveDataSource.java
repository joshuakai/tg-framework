package com.tg.framework.data.commons.masterslave.datasource;

import com.google.common.collect.Iterables;
import com.tg.framework.data.commons.masterslave.context.MasterOnlyContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

public class MasterSlaveDataSource extends AbstractRoutingDataSource {

  private static final Logger LOGGER = LoggerFactory.getLogger(MasterSlaveDataSource.class);

  private String master;
  private Iterator<String> slaves;
  private MasterOnlyContext context;

  public MasterSlaveDataSource(String masterName, boolean considerMasterAsSlave,
      Map<String, DataSource> datasourceMap, MasterOnlyContext context) {
    Assert.notEmpty(datasourceMap, "At least one datasource must be provided.");
    Assert.isTrue(StringUtils.isNotBlank(masterName), "Invalid datasource name.");
    DataSource master = datasourceMap.get(masterName);
    Assert.notNull(master, String.format("Master datasource '%s' must be provided.", masterName));
    List<String> slaves = new ArrayList<>();
    datasourceMap.forEach((k, v) -> {
      Assert.isTrue(StringUtils.isNotBlank(k), "Invalid datasource name.");
      Assert.notNull(v, String.format("Null value found for datasource '%s'.", k));
      if (considerMasterAsSlave || !StringUtils.equals(masterName, k)) {
        slaves.add(k);
      }
    });
    Assert.notNull(context, "MasterOnlyContext must not be null.");
    setDefaultTargetDataSource(master);
    setTargetDataSources(new HashMap<>(datasourceMap));
    this.master = masterName;
    this.slaves = Iterables.cycle(slaves).iterator();
    this.context = context;
  }

  @Override
  protected Object determineCurrentLookupKey() {
    String lookupKey = null;
    if (context.isMasterOnly()) {
      LOGGER.debug("Master-only context found, using master.");
      lookupKey = master;
    } else {
      if (slaves.hasNext()) {
        lookupKey = slaves.next();
      }
      if (lookupKey == null) {
        lookupKey = master;
        LOGGER.debug("No available slaves, using master.");
      } else {
        LOGGER.debug("Slaves found.");
      }
    }
    LOGGER.debug("Actual datasource '{}'.", lookupKey);
    return lookupKey;
  }

}
