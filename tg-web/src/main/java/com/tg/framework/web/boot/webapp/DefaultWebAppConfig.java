package com.tg.framework.web.boot.webapp;

import com.tg.framework.web.boot.auditing.EnableDefaultAuditing;
import com.tg.framework.web.boot.cache.EnableDefaultCaching;
import com.tg.framework.web.boot.cache.EnableRedisLock;
import com.tg.framework.web.boot.mvc.EnableDefaultWebMvc;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDefaultWebMvc
@EnableDefaultAuditing
@EnableDefaultCaching
@EnableRedisLock
public class DefaultWebAppConfig {

}
