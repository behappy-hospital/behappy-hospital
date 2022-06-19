package org.xiaowu.behappy.msm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xiaowu.behappy.common.rmq.contant.MqConst;

@Configuration
@RequiredArgsConstructor
public class MqConfiguration {

    /**
     * 交换机
     * @return
     */
    @Bean
    public Exchange sendMsmExchange() {
        return new DirectExchange(MqConst.EXCHANGE_DIRECT_MSM, true, false);
    }

    /**
     * 发送短信队列
     * @return
     */
    @Bean
    public Queue sendMsmQueue() {
        Queue queue = new Queue(MqConst.QUEUE_MSM_ITEM, true, false, false);
        return queue;
    }

    /**
     * 创建发送短信的binding,
     */
    @Bean
    public Binding sendMsmBinding() {
        return new Binding(MqConst.QUEUE_MSM_ITEM, Binding.DestinationType.QUEUE,
                MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, null);
    }

}