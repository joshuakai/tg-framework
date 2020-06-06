package com.tg.framework.commons.data.jdbc;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

  private static Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

  private DynamicDataSourceContext context;

  public DynamicDataSource(DynamicDataSourceContext context, DataSource master, DataSource slave) {
    this.context = context;
    Map<Object, Object> map = new HashMap<>(2);
    map.put(DynamicDataSourceLookupKey.MASTER, master);
    map.put(DynamicDataSourceLookupKey.SLAVE, slave);
    setTargetDataSources(map);
    setDefaultTargetDataSource(master);
  }

  @Override
  protected Object determineCurrentLookupKey() {
    DynamicDataSourceLookupKey lookupKey = context.get();
    logger.debug("Determine current lookup key {}.", lookupKey);
    return lookupKey;
  }

}
