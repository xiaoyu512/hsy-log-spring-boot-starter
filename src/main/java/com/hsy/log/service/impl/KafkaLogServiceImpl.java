package com.hsy.log.service.impl;

import com.hsy.log.property.LogProperties;
import com.hsy.log.domain.OperationLog;
import com.hsy.log.service.LogService;
import com.hsy.log.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 日志消息kafka接口实现
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/12/17 20:26
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "hsy.log", name = "type", havingValue = "kafka")
public class KafkaLogServiceImpl implements LogService {
    private final LogProperties logProperties;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaLogServiceImpl(LogProperties logProperties, KafkaTemplate<String, String> kafkaTemplate) {
        this.logProperties = logProperties;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void logOutput(OperationLog operationLog) {
        MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(JSONUtil.toJson(operationLog));
        messageBuilder.setHeader(KafkaHeaders.TOPIC, logProperties.getKafka().getTopic());
        messageBuilder.setHeader(KafkaHeaders.MESSAGE_KEY, logProperties.getKafka().getKey());
        Map<String, String> headers = logProperties.getKafka().getHeaders();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(messageBuilder::setHeader);
        }
        kafkaTemplate.send(messageBuilder.build());
        if (logProperties.isConsole()) {
            log.info("~~~~~~调用方法[{}]的结果: {}", operationLog.getClassMethod(), JSONUtil.toJson(operationLog));
        }
    }
}
