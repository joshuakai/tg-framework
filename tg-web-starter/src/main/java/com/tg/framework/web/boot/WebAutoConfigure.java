package com.tg.framework.web.boot;

import com.tg.framework.web.boot.auditing.AuditingAutoConfigure;
import com.tg.framework.web.boot.cache.CacheAutoConfigure;
import com.tg.framework.web.boot.http.HttpClientAutoConfigure;
import com.tg.framework.web.boot.http.RestTemplateAutoConfigure;
import com.tg.framework.web.boot.http.XForwardedAutoConfigure;
import com.tg.framework.web.boot.jdbc.MasterSlaveDataSourceAutoConfigure;
import com.tg.framework.web.boot.jpa.JpaAutoConfigure;
import com.tg.framework.web.boot.lock.RedisLockAutoConfigure;
import com.tg.framework.web.boot.lock.ScheduledClusterAutoConfigure;
import com.tg.framework.web.boot.mvc.SecurityWebMvcAutoConfigure;
import com.tg.framework.web.boot.mvc.WebMvcAutoConfigure;
import com.tg.framework.web.boot.task.RedisMutexTaskAutoConfigure;
import com.tg.framework.web.boot.transaction.TransactionInterceptorAutoConfigure;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    MasterSlaveDataSourceAutoConfigure.class,
    JpaAutoConfigure.class,
    TransactionInterceptorAutoConfigure.class,
    CacheAutoConfigure.class,
    AuditingAutoConfigure.class,
    XForwardedAutoConfigure.class,
    HttpClientAutoConfigure.class,
    RestTemplateAutoConfigure.class,
    RedisLockAutoConfigure.class,
    ScheduledClusterAutoConfigure.class,
    RedisMutexTaskAutoConfigure.class,
    WebMvcAutoConfigure.class,
    SecurityWebMvcAutoConfigure.class
})
public class WebAutoConfigure {

}
