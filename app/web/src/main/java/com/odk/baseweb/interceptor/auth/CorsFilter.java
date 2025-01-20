package com.odk.baseweb.interceptor.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serial;

/**
 * CorsFilter 后端使用了过滤器（Filter）或拦截器（Interceptor），可能会拦截 OPTIONS 请求，导致预检请求失败。
 *
 * 解决方案：
 * 确保 OPTIONS 请求不会被拦截。可以在过滤器中添加以下逻辑：
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/20
 */
@Component
public class CorsFilter extends HttpFilter {

    @Serial
    private static final long serialVersionUID = -7627329498296815315L;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 允许 OPTIONS 请求通过
        if ("OPTIONS".equals(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Odk-Token, *");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(request, response);
    }
}
