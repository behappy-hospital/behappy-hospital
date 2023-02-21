package org.xiaowu.behappy.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
@EnableFeignClients(basePackages = "org.xiaowu.behappy")
public class BehappyStatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyStatisticsApplication.class, args);
    }

}
