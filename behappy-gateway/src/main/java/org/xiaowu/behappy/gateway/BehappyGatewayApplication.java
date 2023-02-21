package org.xiaowu.behappy.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiaowu
 */
@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
public class BehappyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyGatewayApplication.class, args);
    }

}
