package com.tg.framework.web.boot;

import com.tg.framework.web.boot.auditing.EnableDefaultAuditing;
import com.tg.framework.web.boot.cache.EnableDefaultCaching;
import com.tg.framework.web.boot.cache.EnableRedisLock;
import com.tg.framework.web.boot.mvc.EnableDefaultWebMvc;
import com.tg.framework.web.boot.task.EnableRedisUniqueTask;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDefaultWebMvc
@EnableDefaultAuditing
@EnableDefaultCaching
@EnableRedisLock
@EnableRedisUniqueTask
public class DefaultWebAppConfig {

}
