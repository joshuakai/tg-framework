package com.tg.framework.web.boot.task;

import com.tg.framework.commons.concurrent.task.MutexTask;
import com.tg.framework.commons.concurrent.task.MutexTaskService;
import com.tg.framework.commons.concurrent.task.redis.RedisMutexTaskService;
import java.util.concurrent.ExecutorService;
import javax.annotation.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ConfigurationProperties("tg.redis.mutex-task")
public class DefaultRedisMutexTaskConfig {

  private String keyPrefix = "mutex_tasks:";

  @Bean
  public MutexTaskService mutexTaskService(RedisTemplate redisTemplate,
      ExecutorService executorService) {
    return new RedisMutexTaskService(redisTemplate, keyPrefix, executorService);
  }

  public String getKeyPrefix() {
    return keyPrefix;
  }

  public void setKeyPrefix(String keyPrefix) {
    this.keyPrefix = keyPrefix;
  }

  @RestController
  static class MutexTaskController {

    @Resource
    private MutexTaskService mutexTaskService;

    @GetMapping("/mutex-tasks/{key}")
    public MutexTask get(@PathVariable String key) {
      return mutexTaskService.get(key);
    }

  }

}
