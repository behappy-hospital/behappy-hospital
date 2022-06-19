package org.xiaowu.behappy.msm.config;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.common.vo.MsmVo;
import org.xiaowu.behappy.common.rmq.contant.MqConst;
import org.xiaowu.behappy.msm.service.MsmService;

/**
 * @author xiaowu
 */
@Component
@RabbitListener(queues = MqConst.QUEUE_MSM_ITEM)
@RequiredArgsConstructor
public class SmsReceiver {

    private final MsmService msmService;

    @RabbitHandler
    public void send(MsmVo msmVo, Message message, Channel channel) {
        msmService.send(msmVo);
    }

}
