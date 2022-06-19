package org.xiaowu.behappy.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
@EnableFeignClients(basePackages = {"org.xiaowu.behappy"})
public class BehappyOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyOrderApplication.class, args);
    }

}
