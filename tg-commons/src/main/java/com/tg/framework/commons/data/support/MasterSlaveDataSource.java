package com.tg.framework.commons.data.support;

import com.google.common.collect.Iterables;
import com.tg.framework.commons.data.MasterOnlyContext;
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
    Assert.notEmpty(datasourceMap, "At least one datasource must be set");
    Assert.hasText(masterName, "Master name must not be null or empty");
    DataSource master = datasourceMap.get(masterName);
    Assert.notNull(master,
        String.format("Master datasource '%s' must be set in the datasource map", masterName));
    List<String> slaves = new ArrayList<>();
    datasourceMap.forEach((k, v) -> {
      Assert.hasText(k, "Datasource name must not be null or empty");
      Assert.notNull(v, String.format("Datasource '%s' must be set in the datasource map", k));
      if (considerMasterAsSlave || !StringUtils.equals(masterName, k)) {
        slaves.add(k);
      }
    });
    Assert.notNull(context, "A master only context must be set");
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
      try {
        lookupKey = slaves.next();
      } catch (Exception e) {
        LOGGER.warn("An exception occurred while rounding look up key", e);
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
