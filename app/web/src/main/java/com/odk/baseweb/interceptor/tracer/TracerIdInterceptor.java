package com.odk.baseweb.interceptor.tracer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * TracerIdInterceptor
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/7
 */
public class TracerIdInterceptor implements HandlerInterceptor {

    private static final String TRACER_ID = "traceId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(TRACER_ID, UUID.randomUUID().toString());
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
    }
}
