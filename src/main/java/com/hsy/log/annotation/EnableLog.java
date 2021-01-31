package com.hsy.log.annotation;

import com.hsy.log.config.LogConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用日志组件
 *
 * @author huoshengyu
 * @version 1.0
 * @date 2020/12/17 15:26
 */
@Import(LogConfig.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableLog {
}
