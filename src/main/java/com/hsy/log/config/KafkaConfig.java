package com.hsy.log.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * kafka配置
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/12/22 14:35
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "hsy.log", name = "type", havingValue = "kafka")
public class KafkaConfig {

}
