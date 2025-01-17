package com.odk.baseweb.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;


/**
 * CorsInterceptor
 * 废弃，使用addCorsMappings(CorsRegistry registry)实现
 *
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/1/27
 */
@Deprecated
public class CorsInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 动态设置允许的域名
//        String origin = request.getHeader("Origin");
//        if (origin != null && allowedOrigins.contains(origin)) {
//            response.setHeader("Access-Control-Allow-Origin", origin);
//        }
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
//        response.setHeader("Access-Control-Max-Age", "86400");
//        response.setHeader("Access-Control-Allow-Headers", "*");
//
//        // 如果是OPTIONS则结束请求
//        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
//            response.setStatus(HttpStatus.NO_CONTENT.value());
//            return false;
//        }
//
        return true;
    }


    // 允许的域名列表
    private static final List<String> allowedOrigins = Arrays.asList(
//            "https://localhost",
            "https://sub.example.com"
    );

}
