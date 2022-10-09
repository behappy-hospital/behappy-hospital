package org.xiaowu.behappy.executor.task.service.jobhandler;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.common.rmq.contant.MqConst;
import org.xiaowu.behappy.executor.task.core.config.XxlJobLogger;

/**
 * 每天8点执行 提醒就诊
 * @author xiaowu
 */
@Component
@RequiredArgsConstructor
public class ReminderXxlJob {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 0 0 8 * * ?
     * @throws Exception
     */
    @XxlJob("reminderJobHandler")
    public void reminderJobHandler() throws Exception {
        XxlJobLogger.log("执行任务: 提醒就诊");
        rabbitTemplate.convertAndSend(MqConst.EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_8, "");
        // default success
    }

}
