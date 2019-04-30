package com.tg.framework.cloud.boot;

import com.tg.framework.web.boot.DefaultWebAppConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DefaultWebAppConfig.class)
public class DefaultCloudAppConfig {

}
