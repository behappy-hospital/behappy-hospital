package org.xiaowu.behappy.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
@EnableDiscoveryClient
public class BehappyManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyManagerApplication.class, args);
    }

}
