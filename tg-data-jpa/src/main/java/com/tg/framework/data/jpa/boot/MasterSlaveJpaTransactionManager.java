package com.tg.framework.data.jpa.boot;

import com.tg.framework.data.commons.masterslave.context.MasterOnlyContext;
import javax.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.util.Assert;

public class MasterSlaveJpaTransactionManager extends JpaTransactionManager {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MasterSlaveJpaTransactionManager.class);

  private MasterOnlyContext context;

  public MasterSlaveJpaTransactionManager(MasterOnlyContext context) {
    super();
    this.context = context;
  }

  public MasterSlaveJpaTransactionManager(EntityManagerFactory emf,
      MasterOnlyContext context) {
    super(emf);
    Assert.notNull(context, "MasterOnlyContext must not be null.");
    this.context = context;
  }

  @Override
  protected void doBegin(Object transaction, TransactionDefinition definition) {
    boolean masterOnly = !definition.isReadOnly();
    masterOnly = context.mark(masterOnly);
    LOGGER.debug("Mark master-only as {}.", masterOnly);
    try {
      super.doBegin(transaction, definition);
    } catch (Exception e) {
      masterOnly = context.reset();
      LOGGER.debug("Reset master-only as {}.", masterOnly);
      throw e;
    }
  }

  @Override
  protected void doCleanupAfterCompletion(Object transaction) {
    super.doCleanupAfterCompletion(transaction);
    boolean masterOnly = context.reset();
    LOGGER.debug("Reset master-only as {}.", masterOnly);
  }

}
