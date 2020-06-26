package com.tg.framework.web.boot;

import com.tg.framework.web.boot.auditing.AuditingAutoConfigure;
import com.tg.framework.web.boot.concurrent.lock.RedisLockAutoConfigure;
import com.tg.framework.web.boot.concurrent.lock.ScheduledClusterAutoConfigure;
import com.tg.framework.web.boot.concurrent.task.RedisMutexTaskAutoConfigure;
import com.tg.framework.web.boot.data.CacheAutoConfigure;
import com.tg.framework.web.boot.data.JpaAutoConfigure;
import com.tg.framework.web.boot.data.MasterSlaveDataSourceAutoConfigure;
import com.tg.framework.web.boot.data.TransactionInterceptorAutoConfigure;
import com.tg.framework.web.boot.http.HttpClientAutoConfigure;
import com.tg.framework.web.boot.http.RestTemplateAutoConfigure;
import com.tg.framework.web.boot.http.XForwardedAutoConfigure;
import com.tg.framework.web.boot.mvc.GeneralWebMvcAutoConfigure;
import com.tg.framework.web.boot.mvc.JpaWebMvcAutoConfigure;
import com.tg.framework.web.boot.mvc.SecurityWebMvcAutoConfigure;
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
    GeneralWebMvcAutoConfigure.class,
    JpaWebMvcAutoConfigure.class,
    SecurityWebMvcAutoConfigure.class
})
public class WebAutoConfigure {

}
