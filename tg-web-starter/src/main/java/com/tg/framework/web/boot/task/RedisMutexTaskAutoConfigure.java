package com.tg.framework.web.boot.task;

import com.tg.framework.commons.concurrent.task.MutexTask;
import com.tg.framework.commons.concurrent.task.MutexTaskService;
import com.tg.framework.commons.concurrent.task.redis.RedisMutexTaskService;
import java.util.concurrent.ExecutorService;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ConditionalOnClass({RedisTemplate.class})
@ConditionalOnProperty(prefix = "tg.redis-mutex-task", value = "enabled")
@EnableConfigurationProperties(RedisMutexTaskProperties.class)
public class RedisMutexTaskAutoConfigure {

  @Bean
  @ConditionalOnMissingBean
  public MutexTaskService mutexTaskService(RedisMutexTaskProperties redisMutexTaskProperties,
      RedisTemplate redisTemplate, ExecutorServiceFactory executorServiceFactory) {
    return new RedisMutexTaskService(redisTemplate, redisMutexTaskProperties.getKeyPrefix(),
        executorServiceFactory.get());
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

  public interface ExecutorServiceFactory {

    ExecutorService get();

  }

}
