package org.xiaowu.behappy.msm.receiver;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.api.common.vo.MsmVo;
import org.xiaowu.behappy.common.core.exception.HospitalException;
import org.xiaowu.behappy.common.rmq.contant.MqConst;
import org.xiaowu.behappy.msm.service.MsmService;

import java.io.IOException;

/**
 * @author xiaowu
 */
@Slf4j
@Component
@RabbitListener(queues = MqConst.QUEUE_MSM_ITEM)
@RequiredArgsConstructor
public class SmsReceiver {

    private final MsmService msmService;

    @RabbitHandler
    public void send(MsmVo msmVo, Message message, Channel channel) throws IOException {
        try {
            log.info("{} 队列收到消息, 内容为: {}",MqConst.QUEUE_MSM_ITEM,msmVo.toString());
            boolean successSend = msmService.send(msmVo);
            if (!successSend) {
                throw new HospitalException("队列发送短信失败", 201);
            }
            // 手动调用支付宝收单
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("队列: {}, 短信模块报错: {}", MqConst.QUEUE_MSM_ITEM, e.getMessage());
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}
