package org.xiaowu.behappy.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author xiaowu
 */
@EnableDiscoveryClient
@SpringBootApplication
public class BehappyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyGatewayApplication.class, args);
    }

}
