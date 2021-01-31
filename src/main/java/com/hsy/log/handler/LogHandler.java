package com.hsy.log.handler;

import com.hsy.log.annotation.Log;
import com.hsy.log.enums.ResultType;
import com.hsy.log.service.LogService;
import com.hsy.log.util.JSONUtil;
import com.hsy.log.domain.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义日志注解实现类
 *
 * @author HuoShengyu
 * @version 1.0
 * @date 2018-07-10 17:56:35
 */
@Slf4j
@Aspect
@Component
public class LogHandler {
    /**
     * 日期格式化
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");

    private final LogService logService;

    public LogHandler(LogService logService) {
        this.logService = logService;
    }

    /**
     * 拦截注解Log的方法
     *
     * @param joinPoint 切点对象
     * @param annLog    日志注解
     * @return Object 返回对象
     * @throws Throwable
     */
    @Around("@annotation(annLog)")
    public Object logTime(ProceedingJoinPoint joinPoint, Log annLog) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(Thread.currentThread().getName());
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        String classMethod = String.format("%s.%s", ms.getDeclaringTypeName(), ms.getName());
        OperationLog.OperationLogBuilder logBuilder = OperationLog.builder();
        try {
            logBuilder.classMethod(classMethod)
                    .logType(annLog.logType())
                    .traceId(MDC.get("traceId"))
                    .description(annLog.value())
                    .logTime(LocalDateTime.now().format(DATE_TIME_FORMATTER));
            HttpServletRequest request = getRequest();
            if (request != null) {
                logBuilder.ip(getIpAddress(request))
                        .url(request.getRequestURI())
                        .httpMethod(request.getMethod())
                        .clientId(request.getHeader("Client-Id"))
                        .requestUser(request.getHeader("Request-User"));
            }
        } catch (Exception e) {
            log.error("~~~~~~调用方法[{}]创建OperationLogBuilder失败: ", classMethod, e);
        }
        try {
            // 打印输入日志
            if (annLog.input()) {
                inputLog(joinPoint);
            }
            Object obj = joinPoint.proceed();
            // 打印输出日志
            if (annLog.output()) {
                outputLog(joinPoint, obj);
            }
            logBuilder.resultType(ResultType.SUCCESS);
            return obj;
        } catch (Exception exception) {
            logBuilder.resultType(ResultType.FAILURE);
            throw exception;
        } finally {
            stopWatch.stop();
            logBuilder.elapsedTime(stopWatch.getTotalTimeMillis());
            logService.logOutput(logBuilder.build());
        }
    }

    /**
     * 打印输入日志
     *
     * @param joinPoint 切点参数
     */
    private void inputLog(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String classMethod = String.format("%s.%s", signature.getDeclaringTypeName(), signature.getName());
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            int index = 0;
            for (Object obj : args) {
                if (obj instanceof HttpServletRequest
                        || obj instanceof HttpServletResponse) {
                    log.info("~~~~~~请求类方法[{}]入参,参数[{}] -> {}", classMethod, index++, obj.toString());
                } else if (obj instanceof MultipartFile) {
                    MultipartFile mf = (MultipartFile) obj;
                    log.info("~~~~~~请求类方法[{}]入参,参数[{}] -> {}", classMethod, index++, mf.getOriginalFilename());
                } else if (obj instanceof MultipartFile[]) {
                    MultipartFile[] mfArr = (MultipartFile[]) obj;
                    List<String> fileNames = new ArrayList<>();
                    for (MultipartFile mf : mfArr) {
                        fileNames.add(mf.getOriginalFilename());
                    }
                    log.info("~~~~~~请求类方法[{}]入参,参数[{}] -> {}", classMethod, index++, JSONUtil.toJson(fileNames));
                } else {
                    log.info("~~~~~~请求类方法[{}]入参,参数[{}] -> {}", classMethod, index++, JSONUtil.toJson(obj));
                }
            }
        }
    }

    /**
     * 打印输出日志
     *
     * @param joinPoint 切点参数
     * @param obj       返回参数
     */
    private void outputLog(ProceedingJoinPoint joinPoint, Object obj) {
        Signature signature = joinPoint.getSignature();
        String classMethod = String.format("%s.%s", signature.getDeclaringTypeName(), signature.getName());
        log.info("~~~~~~请求类方法[{}]返回参数: [{}]", classMethod, JSONUtil.toJson(obj));
    }

    /**
     * 获取HttpServletRequest对象
     *
     * @return HttpServletRequest
     */
    private HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes != null ? ((ServletRequestAttributes) requestAttributes).getRequest() : null;
    }

    /**
     * 获取ip
     *
     * @param request 请求
     * @return String
     * @throws IOException
     */
    public static String getIpAddress(HttpServletRequest request) throws IOException {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.length() > 15) {
                String[] ips = ip.split(",");
                for (int index = 0; index < ips.length; ++index) {
                    String strIp = ips[index];
                    if (!"unknown".equalsIgnoreCase(strIp)) {
                        ip = strIp;
                        break;
                    }
                }
            }
        } else {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }
}