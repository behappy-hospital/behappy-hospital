package org.xiaowu.behappy.hosp.receiver;

import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.common.vo.MsmVo;
import org.xiaowu.behappy.api.hosp.vo.OrderMqVo;
import org.xiaowu.behappy.common.rmq.contant.MqConst;
import org.xiaowu.behappy.hosp.entity.Schedule;
import org.xiaowu.behappy.hosp.service.ScheduleService;

import java.io.IOException;

/**
 *
 * @author xiaowu
 */
@Slf4j
@Component
@RabbitListener(queues = MqConst.QUEUE_ORDER)
@AllArgsConstructor
public class HospitalReceiver {

    private final ScheduleService scheduleService;

    private final RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
        try {
            log.info("{} 队列收到消息, 内容为: {}",MqConst.QUEUE_ORDER,orderMqVo.toString());
            Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
            if (null != orderMqVo.getAvailableNumber()) {
                //下单成功更新预约数
                schedule.setReservedNumber(orderMqVo.getReservedNumber());
                schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
            } else {
                //取消预约更新预约数
                int availableNumber = schedule.getAvailableNumber() + 1;
                schedule.setAvailableNumber(availableNumber);
            }
            scheduleService.update(schedule);
            //发送短信
            MsmVo msmVo = orderMqVo.getMsmVo();
            if (null != msmVo) {
                rabbitTemplate.convertAndSend(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("队列: {}, 医院模块报错: {}",MqConst.QUEUE_ORDER,e.getMessage());
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
