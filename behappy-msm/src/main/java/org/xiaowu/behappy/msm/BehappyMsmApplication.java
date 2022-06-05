package org.xiaowu.behappy.msm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
@EnableDiscoveryClient
public class BehappyMsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyMsmApplication.class, args);
    }

}
