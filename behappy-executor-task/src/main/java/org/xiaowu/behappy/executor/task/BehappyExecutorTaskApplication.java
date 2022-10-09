package org.xiaowu.behappy.executor.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
@EnableDiscoveryClient
public class BehappyExecutorTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyExecutorTaskApplication.class, args);
    }

}

