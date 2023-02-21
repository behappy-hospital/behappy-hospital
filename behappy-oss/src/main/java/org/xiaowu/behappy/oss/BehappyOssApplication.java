package org.xiaowu.behappy.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
public class BehappyOssApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyOssApplication.class, args);
    }

}
