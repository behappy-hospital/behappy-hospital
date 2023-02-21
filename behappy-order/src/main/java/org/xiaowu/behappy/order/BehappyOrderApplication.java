package org.xiaowu.behappy.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = "org.xiaowu.behappy")
@EnableFeignClients(basePackages = {"org.xiaowu.behappy"})
public class BehappyOrderApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(BehappyOrderApplication.class, args);
        System.out.println(Arrays.asList(run.getBeanDefinitionNames()));
    }

}
