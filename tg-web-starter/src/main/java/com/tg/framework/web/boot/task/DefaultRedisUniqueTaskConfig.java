package com.tg.framework.web.boot.task;

import com.tg.framework.commons.concurrent.task.UniqueTask;
import com.tg.framework.commons.concurrent.task.UniqueTaskService;
import com.tg.framework.commons.concurrent.task.redis.RedisUniqueTaskService;
import javax.annotation.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ConfigurationProperties("tg.redis.unique-task")
public class DefaultRedisUniqueTaskConfig {

  private String keyPrefix = "unique_tasks:";

  @Bean
  public UniqueTaskService uniqueTaskService(RedisTemplate redisTemplate) {
    return new RedisUniqueTaskService(redisTemplate, keyPrefix);
  }

  public String getKeyPrefix() {
    return keyPrefix;
  }

  public void setKeyPrefix(String keyPrefix) {
    this.keyPrefix = keyPrefix;
  }

  @RestController
  static class UniqueTaskController {

    @Resource
    private UniqueTaskService uniqueTaskService;

    @GetMapping("/unique-tasks/{key}")
    public UniqueTask get(@PathVariable String key) {
      return uniqueTaskService.get(key);
    }

  }

}
