package org.xiaowu.behappy.hosp.config;

import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.common.vo.MsmVo;
import org.xiaowu.behappy.api.hosp.model.Schedule;
import org.xiaowu.behappy.api.hosp.vo.OrderMqVo;
import org.xiaowu.behappy.common.rmq.contant.MqConst;
import org.xiaowu.behappy.hosp.service.ScheduleService;

import java.io.IOException;

/**
 *
 * @author xiaowu
 */
@Component
@RabbitListener(queues = MqConst.QUEUE_ORDER)
@AllArgsConstructor
public class HospitalReceiver {

    private final ScheduleService scheduleService;

    private final RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void receive(OrderMqVo orderMqVo){
        //下单成功更新预约数
        Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
        schedule.setReservedNumber(orderMqVo.getReservedNumber());
        schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
        scheduleService.update(schedule);
        //发送短信
        MsmVo msmVo = orderMqVo.getMsmVo();
        if(null != msmVo) {
            rabbitTemplate.convertAndSend(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
        }
    }

    @RabbitHandler
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
        if(null != orderMqVo.getAvailableNumber()) {
            //下单成功更新预约数
            Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
            schedule.setReservedNumber(orderMqVo.getReservedNumber());
            schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
            scheduleService.update(schedule);
        } else {
            //取消预约更新预约数
            Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
            int availableNumber = schedule.getAvailableNumber().intValue() + 1;
            schedule.setAvailableNumber(availableNumber);
            scheduleService.update(schedule);
        }
        //发送短信
        MsmVo msmVo = orderMqVo.getMsmVo();
        if(null != msmVo) {
            rabbitTemplate.convertAndSend(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
        }
    }
}
