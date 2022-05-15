package org.xiaowu.behappy.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.xiaowu.behappy")
public class BehappyCmnApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyCmnApplication.class, args);
    }

}
