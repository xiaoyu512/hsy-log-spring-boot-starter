package com.hsy.log.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 日志配置
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/12/11 14:42
 */
@Slf4j
@Configuration
@Import({RabbitConfig.class, KafkaConfig.class})
@ComponentScan(basePackages = "com.spring.boot.log")
@ServletComponentScan(basePackages = "com.spring.boot.log")
@ConfigurationPropertiesScan(basePackages = "com.spring.boot.log.property")
public class LogConfig {

}
