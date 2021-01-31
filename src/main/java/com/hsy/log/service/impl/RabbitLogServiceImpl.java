package com.hsy.log.service.impl;

import com.hsy.log.property.LogProperties;
import com.hsy.log.domain.OperationLog;
import com.hsy.log.service.LogService;
import com.hsy.log.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * 日志消息rabbit接口实现
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/12/17 20:26
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "hsy.log", name = "type", havingValue = "rabbit")
public class RabbitLogServiceImpl implements LogService {
    private final LogProperties logProperties;
    private final RabbitTemplate rabbitTemplate;

    public RabbitLogServiceImpl(LogProperties logProperties, RabbitTemplate rabbitTemplate) {
        this.logProperties = logProperties;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void logOutput(OperationLog operationLog) {
        MessageProperties messageProperties = new MessageProperties();
        String id = UUID.randomUUID().toString();
        messageProperties.setMessageId(id);
        messageProperties.setCorrelationId(id);
        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
        LogProperties.Rabbit rabbit = logProperties.getRabbit();
        Map<String, String> headers = rabbit.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(messageProperties::setHeader);
        }
        Message message = new Message(JSONUtil.toBytes(operationLog), messageProperties);
        rabbitTemplate.convertAndSend(rabbit.getExchange(), rabbit.getRoutingKey(), message);
        if (logProperties.isConsole()) {
            log.info("~~~~~~调用方法[{}]的结果: {}", operationLog.getClassMethod(), JSONUtil.toJson(operationLog));
        }
    }
}
