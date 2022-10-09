package org.xiaowu.behappy.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
public class BehappyMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyMonitorApplication.class, args);
    }

}
