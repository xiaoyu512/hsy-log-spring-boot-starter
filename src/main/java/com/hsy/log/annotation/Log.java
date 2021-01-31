package com.hsy.log.annotation;

import com.hsy.log.enums.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志打印注解
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/11/18 15:26
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    /**
     * 描述
     */
    String value() default "";

    /**
     * 是否打印输入日志
     */
    boolean input() default false;

    /**
     * 是否打印输出日志
     */
    boolean output() default false;

    /**
     * 打印类型
     */
    LogType logType() default LogType.UNKNOWN;
}
