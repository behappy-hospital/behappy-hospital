package org.xiaowu.behappy.common.core.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 小五
 */
@Slf4j
@Data
@EnableAsync
@Configuration
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadConfig implements AsyncConfigurer {

    /**
     * 获取当前机器的核数, 不一定准确 需根据实际场景 CPU密集 || IO 密集
     */
    public static final int cpuNum = Runtime.getRuntime().availableProcessors();

    private Integer coreSize = cpuNum;
    private Integer maxSize = cpuNum * 2;
    private Integer queueCapacity = 500;
    private Integer keepAliveTime = 60;
    private Integer awaitTerminationSeconds = 60;
    private String namePrefix = "custom";

    @Override
    public Executor getAsyncExecutor() {
        return getThreadPoolTaskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            log.error("Error Occurs in async method:{}", ex.getMessage());
        };
    }

    @Bean
    public ThreadPoolTaskExecutor executor(){
        return getThreadPoolTaskExecutor();
    }

    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程大小 默认区 CPU 数量
        taskExecutor.setCorePoolSize(coreSize);
        // 最大线程大小 默认区 CPU * 2 数量
        taskExecutor.setMaxPoolSize(maxSize);
        // 队列最大容量
        taskExecutor.setQueueCapacity(queueCapacity);
        // 拒绝策略 默认new ThreadPoolExecutor.AbortPolicy()
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 任务结束再shutdown
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程最大空闲时间
        taskExecutor.setKeepAliveSeconds(keepAliveTime);
        taskExecutor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        taskExecutor.setThreadNamePrefix(namePrefix);
        // 交给spring托管的会自动初始化，因为实现了InitializingBean接口
        taskExecutor.initialize();
        return taskExecutor;
    }

}
