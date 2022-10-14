package org.xiaowu.behappy.common.rmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Objects;

import static cn.hutool.core.util.CharsetUtil.UTF_8;

/**
 *
 * @author xiaowu
 */
@Slf4j
@Configuration
@EnableRabbit
public class MqConfiguration {

    private RabbitTemplate rabbitTemplate;

    @Primary
    @Bean
    public RabbitTemplate rabbitTemplate(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.rabbitTemplate = rabbitTemplate;
        initRabbitTemplate();
        return rabbitTemplate;
    }

    /**
     * 消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 1.设置确认回调： ConfirmCallback
     * 先在配置文件中开启 publisher-confirms: true
     *
     *  2.消息抵达队列的确认回调
     * 　	开启发送端消息抵达队列确认
     *     publisher-returns: true
     *     	只要抵达队列，以异步优先回调我们这个 returnconfirm
     *     template:
     *       mandatory: true
     *	3.消费端确认(保证每一个消息被正确消费才可以broker删除消息)
     *		1.默认是自动确认的 只要消息接收到 服务端就会移除这个消息
     *
     *		如何签收:
     *			签收: channel.basicAck(deliveryTag, false);
     *			拒签: channel.basicNack(deliveryTag, false,true);
     *	配置文件中一定要加上这个配置
     *		listener:
     *       simple:
     *         acknowledge-mode: manual
     */
    public void initRabbitTemplate() {
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setEncoding(UTF_8);
        /**
         * 	设置确认回调
         *  correlationData: 消息的唯一id
         *  ack： 消息是否成功收到
         * 	cause：失败的原因
         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("\n确认收到消息: " + Objects.toString(correlationData,"消息队列消息为空!") + "\tack: " + ack + "\tcause： " + cause);
            }
        });
        /**
         * 设置消息抵达队列回调：可以很明确的知道那些消息失败了
         * message: 投递失败的消息详细信息
         * replyCode: 回复的状态码
         * replyText: 回复的文本内容
         * exchange: 当时这个发送给那个交换机
         * routerKey: 当时这个消息用那个路由键
         */
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            Message message = returnedMessage.getMessage();
            String exchange = returnedMessage.getExchange();
            String replyText = returnedMessage.getReplyText();
            int replyCode = returnedMessage.getReplyCode();
            String routerKey = returnedMessage.getRoutingKey();
            log.error("Fail Message [" + message + "]" + "\treplyCode: " + replyCode + "\treplyText:" + replyText + "\texchange:" + exchange + "\trouterKey:" + routerKey);
        });
    }
}
