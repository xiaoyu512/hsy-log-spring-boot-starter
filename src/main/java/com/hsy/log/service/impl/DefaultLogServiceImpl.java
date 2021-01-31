package com.hsy.log.service.impl;

import com.hsy.log.domain.OperationLog;
import com.hsy.log.service.LogService;
import com.hsy.log.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * 日志消息默认接口实现
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/12/18 10:41
 */
@Slf4j
@Service
@ConditionalOnMissingBean({
        KafkaLogServiceImpl.class,
        RabbitLogServiceImpl.class,
        RedisLogServiceImpl.class
})
public class DefaultLogServiceImpl implements LogService {
    @Override
    public void logOutput(OperationLog operationLog) {
        log.info("~~~~~~调用方法[{}]的结果: {}", operationLog.getClassMethod(), JSONUtil.toJson(operationLog));
    }
}
