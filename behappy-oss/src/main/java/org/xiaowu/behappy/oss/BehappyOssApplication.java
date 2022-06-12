package org.xiaowu.behappy.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
public class BehappyOssApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyOssApplication.class, args);
    }

}
