package com.hsy.log.service.impl;

import com.hsy.log.property.LogProperties;
import com.hsy.log.domain.OperationLog;
import com.hsy.log.service.LogService;
import com.hsy.log.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 日志消息redis接口实现
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/12/18 10:41
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "hsy.log", name = "type", havingValue = "redis")
public class RedisLogServiceImpl implements LogService {
    private final LogProperties logProperties;
    private final ListOperations<String, String> listOperations;

    public RedisLogServiceImpl(LogProperties logProperties, StringRedisTemplate stringRedisTemplate) {
        this.logProperties = logProperties;
        this.listOperations = stringRedisTemplate.opsForList();
    }

    @Override
    public void logOutput(OperationLog operationLog) {
        LogProperties.Redis redisProp = logProperties.getRedis();
        String key = redisProp.getKey();
        if (StringUtils.isBlank(key)) {
            log.warn("未配置日志的Redis的key,请检查配置信息");
            return;
        }
        if (redisProp.getMaxCount() != null) {
            checkMaxCount(key, redisProp.getMaxCount());
        } else {
            log.warn("未配置日志的Redis相关的最大条数会导致内存占用过大");
        }
        listOperations.leftPush(key, JSONUtil.toJson(operationLog));
        if (logProperties.isConsole()) {
            log.info("~~~~~~调用方法[{}]的结果: {}", operationLog.getClassMethod(), JSONUtil.toJson(operationLog));
        }
    }

    /**
     * 处理条数
     *
     * @param key      键
     * @param maxCount 最大行数
     */
    private void checkMaxCount(String key, Long maxCount) {
        Long size = listOperations.size(key);
        if (size == null) {
            return;
        }
        if (maxCount.compareTo(size) <= 0) {
            listOperations.rightPop(key);
            checkMaxCount(key, maxCount);
        }
    }
}
