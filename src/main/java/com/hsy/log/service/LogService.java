package com.hsy.log.service;

import com.hsy.log.domain.OperationLog;

/**
 * 日志操作接口
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/12/17 20:26
 */
public interface LogService {
    /**
     * 输出
     *
     * @param operationLog 操作日志
     */
    void logOutput(OperationLog operationLog);
}
