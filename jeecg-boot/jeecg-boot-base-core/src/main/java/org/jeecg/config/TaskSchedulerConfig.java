package org.jeecg.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 任务调度器配置
 * 提供 ThreadPoolTaskScheduler Bean 用于 AI RAG 流程调度等功能
 * 仅当容器中不存在 ThreadPoolTaskScheduler 时才创建
 *
 * @author jeecg
 */
@Slf4j
@Configuration
public class TaskSchedulerConfig {

    @Bean
    @ConditionalOnMissingBean(ThreadPoolTaskScheduler.class)
    public ThreadPoolTaskScheduler taskScheduler() {
        log.info("初始化定时任务调度器 ThreadPoolTaskScheduler");

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("airag-scheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        return scheduler;
    }
}
