package org.xiaowu.behappy.msm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
public class BehappyMsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyMsmApplication.class, args);
    }

}
