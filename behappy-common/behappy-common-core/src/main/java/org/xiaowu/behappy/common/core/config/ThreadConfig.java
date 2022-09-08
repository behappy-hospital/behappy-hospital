package org.xiaowu.behappy.common.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 小五
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadConfig {

    private Integer coreSize = 20;
    private Integer maxSize = 200;
    private Integer keepAliveTime = 10;
    private String namePrefix = "custom";

    @Primary
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                coreSize,
                maxSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100000),
                new MyThreadFactory(namePrefix),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    /**
     * 在DefaultThreadFactory中创建的线程名字格式为pool-m-thread-n,
     * 也就是pool-1-thread-2,pool-2-thread-3,完全看不出该线程为何创建，在做什么事情。在调试、监控和查看日志时非常不便。
     */
    class MyThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        MyThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            if (t.isDaemon())
                t.setDaemon(true);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}