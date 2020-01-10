package com.tg.framework.cloud.boot;

import com.tg.framework.cloud.boot.http.CloudRestTemplateAutoConfigure;
import com.tg.framework.cloud.boot.http.OAuth2RestTemplateAutoConfigure;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CloudRestTemplateAutoConfigure.class, OAuth2RestTemplateAutoConfigure.class})
public class CloudAutoConfigure {

}
