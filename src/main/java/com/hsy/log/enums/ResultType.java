package com.hsy.log.enums;

/**
 * 返回结果
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/11/18 15:38
 */
public enum ResultType {
    SUCCESS("成功"),
    FAILURE("失败");

    private final String description;

    ResultType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
