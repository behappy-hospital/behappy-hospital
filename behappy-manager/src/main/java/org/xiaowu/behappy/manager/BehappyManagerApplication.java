package org.xiaowu.behappy.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 94391
 */
@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
public class BehappyManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyManagerApplication.class, args);
    }

}
