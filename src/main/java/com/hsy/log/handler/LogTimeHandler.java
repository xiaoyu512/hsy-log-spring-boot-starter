package com.hsy.log.handler;

import com.hsy.log.annotation.LogTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 自定义时间打印注解实现类
 *
 * @author HuoShengyu
 * @version 1.0
 * @date 2018-07-10 17:56:35
 */
@Slf4j
@Aspect
@Component
public class LogTimeHandler {
    /**
     * 拦截注解LogTime的方法
     *
     * @param joinPoint 切点对象
     * @return Object 返回对象
     * @throws Throwable
     */
    @Around("@annotation(logTime)")
    public Object logTime(ProceedingJoinPoint joinPoint, LogTime logTime) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object obj = joinPoint.proceed();
        stopWatch.stop();
        log.info("~~~~~~{}执行{}毫秒", logTime.value(), stopWatch.getTotalTimeMillis());
        return obj;
    }
}