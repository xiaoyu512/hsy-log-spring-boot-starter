package com.hsy.log.domain;

import com.hsy.log.enums.LogType;
import com.hsy.log.enums.ResultType;
import lombok.Builder;
import lombok.Data;

/**
 * 操作日志
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/11/18 15:31
 */
@Data
@Builder
public class OperationLog {
    /**
     * 跟踪id
     */
    private String traceId;
    /**
     * 请求ip
     */
    private String ip;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求类型
     */
    private String httpMethod;
    /**
     * 请求类方法
     */
    private String classMethod;
    /**
     * 请求时间
     */
    private String logTime;
    /**
     * 请求客户端标识
     */
    private String clientId;
    /**
     * 请求类型
     */
    private LogType logType;
    /**
     * 用户名
     */
    private String requestUser;
    /**
     * 日志描述
     */
    private String description;
    /**
     * 返回结果
     */
    private ResultType resultType;
    /**
     * 耗时
     */
    private Long elapsedTime;
}
