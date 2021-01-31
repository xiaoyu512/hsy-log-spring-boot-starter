package com.hsy.log.enums;

/**
 * 日志类型枚举
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/11/18 15:03
 */
public enum LogType {
    ADD("添加"),
    DELETE("删除"),
    UPDATE("更新"),
    SELECT("查询"),
    UNKNOWN("未知");

    private final String description;

    LogType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
