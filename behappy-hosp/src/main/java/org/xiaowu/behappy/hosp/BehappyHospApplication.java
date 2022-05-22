package org.xiaowu.behappy.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan(basePackages = "org.xiaowu.behappy")
@SpringBootApplication
public class BehappyHospApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyHospApplication.class, args);
    }

}

