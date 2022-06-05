package org.xiaowu.behappy.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
public class BehappyHospApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyHospApplication.class, args);
    }

}

