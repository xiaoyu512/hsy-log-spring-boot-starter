package com.hsy.log.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * 设置跟踪id
 *
 * @author HuoShengyu
 * @version 1.0
 * @date 2018-06-05 11:11:55
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "traceIdFilter")
public class TraceIdFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (log.isDebugEnabled()) {
            log.debug("初始化过滤器: {}", this.getClass().getName());
        }
    }

    /**
     * 设置日志请求唯一标识
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String traceId = request.getHeader("traceId");
        // 为每个请求设置唯一traceId值
        if (StringUtils.isNotBlank(traceId)) {
            MDC.put("traceId", traceId);
        } else {
            SecureRandom sr = new SecureRandom();
            String requestId = Long.toHexString(System.currentTimeMillis()) + String.valueOf(sr.nextDouble()).substring(2, 8);
            MDC.put("traceId", requestId);
        }
        chain.doFilter(servletRequest, servletResponse);
        MDC.put("traceId", "");
    }

    @Override
    public void destroy() {
        if (log.isDebugEnabled()) {
            log.debug("销毁过滤器: {}", this.getClass().getName());
        }
    }
}
