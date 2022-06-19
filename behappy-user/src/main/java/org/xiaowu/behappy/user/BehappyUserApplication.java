package org.xiaowu.behappy.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"org.xiaowu.behappy"})
@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
public class BehappyUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyUserApplication.class, args);
    }

}
