package com.hsy.log.config;

import com.hsy.log.property.LogProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * rabbit配置
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/12/22 14:35
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "hsy.log", name = "type", havingValue = "rabbit")
public class RabbitConfig {
    private final LogProperties logProperties;

    public RabbitConfig(LogProperties logProperties) {
        this.logProperties = logProperties;
    }

    /**
     * 定义队列
     */
    @Bean(name = "logQueue")
    public Queue logQueue() {
        return QueueBuilder.durable(logProperties.getRabbit().getQueue()).build();
    }

    /**
     * 定义交换机类型为Direct
     */
    @Bean(name = "logExchange")
    public Exchange logExchange() {
        return ExchangeBuilder.directExchange(logProperties.getRabbit().getExchange()).durable(true).build();
    }

    /**
     * 绑定队列和交换机并设置routingKey
     *
     * @param logQueue    队列
     * @param logExchange 交换机
     */
    @Bean
    public Binding binding(@Qualifier("logQueue") Queue logQueue, @Qualifier("logExchange") Exchange logExchange) {
        return BindingBuilder.bind(logQueue).to(logExchange).with(logProperties.getRabbit().getRoutingKey()).noargs();
    }

    /**
     * 设置RabbitTemplate回调函数
     */
    @Component
    static class RabbitCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
        @Autowired
        private RabbitTemplate rabbitTemplate;

        @PostConstruct
        public void init() {
            //指定ConfirmCallback,消息确认后触发
            rabbitTemplate.setConfirmCallback(this);
            //指定ReturnCallback,发送的routingKey不存在触发
            rabbitTemplate.setReturnsCallback(this);
        }

        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            log.info("消息唯一标识: {}", correlationData);
            log.info("确认结果: {}", ack);
            log.info("失败原因: {}", cause);
        }

        @Override
        public void returnedMessage(ReturnedMessage message) {
            log.info("消息主体 message: {}", message.getMessage());
            log.info("应答码 replyCode: {}", message.getReplyCode());
            log.info("应答信息 replyText: {}", message.getReplyText());
            log.info("消息使用的交换器 exchange: {}", message.getExchange());
            log.info("消息使用的路由键 routingKey: {}", message.getRoutingKey());
        }
    }
}
