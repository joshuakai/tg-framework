package com.tg.framework.web.boot.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MasterSlaveJpaTransactionManagerAutoConfigure.class, JpaQueryDslAutoConfigure.class})
public class JpaAutoConfigure {

}
