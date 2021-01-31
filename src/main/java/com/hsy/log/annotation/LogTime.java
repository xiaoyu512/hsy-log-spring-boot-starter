package com.hsy.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解记录方法执行时间
 *
 * @author HuoShengyu
 * @version 1.0
 * @date 2018-07-10 16:47:44
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogTime {
    /**
     * 方法描述
     */
    String value();
}
