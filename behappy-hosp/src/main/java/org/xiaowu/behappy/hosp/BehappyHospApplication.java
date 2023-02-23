package org.xiaowu.behappy.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"org.xiaowu.behappy"})
@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
public class BehappyHospApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyHospApplication.class, args);
    }
}

