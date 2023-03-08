package org.xiaowu.behappy.order.receiver;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.common.rmq.contant.MqConst;
import org.xiaowu.behappy.order.service.OrderService;

import java.io.IOException;

/**
 * 提醒就诊
 * @author 94391
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PatientTipsReceiver {

    private final OrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_TASK_8, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_TASK),
            key = {MqConst.ROUTING_TASK_8}
    ))
    public void patientTips(Message message, Channel channel) throws IOException {

        try {
            log.info("{} 队列收到消息, 内容为: {}",MqConst.QUEUE_TASK_8);
            orderService.patientTips();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("队列: {}, 定时任务模块报错: {}",MqConst.QUEUE_TASK_8,e.getMessage());
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
