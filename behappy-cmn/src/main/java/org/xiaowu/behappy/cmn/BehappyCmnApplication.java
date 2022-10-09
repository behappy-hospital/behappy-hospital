package org.xiaowu.behappy.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
@EnableDiscoveryClient
public class BehappyCmnApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyCmnApplication.class, args);
    }

}
