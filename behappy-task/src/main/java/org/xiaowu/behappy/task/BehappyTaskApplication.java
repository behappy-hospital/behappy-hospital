package org.xiaowu.behappy.task;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.xiaowu.behappy.common.rmq.contant.MqConst;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class BehappyTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyTaskApplication.class, args);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 每天8点执行 提醒就诊
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0/30 * * * * ?")
    public void task1() {
        rabbitTemplate.convertAndSend(MqConst.EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_8, "");
    }

}
