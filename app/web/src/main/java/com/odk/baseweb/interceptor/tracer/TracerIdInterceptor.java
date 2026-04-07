package com.odk.baseweb.interceptor.tracer;

import com.odk.base.util.JacksonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * TracerIdInterceptor
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/7
 */
@Slf4j
public class TracerIdInterceptor implements HandlerInterceptor {

    private static final String TRACER_ID = "traceId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(TRACER_ID, UUID.randomUUID().toString());
        // 【核心修改】不要直接打印 request 对象，而是提取需要的信息

        // 2. 提取请求信息（安全脱敏版）
        Map<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("method", request.getMethod());
        requestInfo.put("url", request.getRequestURL().toString());
        requestInfo.put("uri", request.getRequestURI());
        requestInfo.put("remoteAddr", request.getRemoteAddr());
        requestInfo.put("headers", getHeaders(request));

        // 3. 处理参数：过滤掉文件对象
//        Map<String, Object> safeParams = new HashMap<>();
//        request.getParameterMap().forEach((key, values) -> {
//            // 核心逻辑：如果参数值是 MultipartFile，则不打印具体值，只打印占位符
//            // 注意：getParameterMap() 返回的是 String[]，所以这里主要防的是普通字符串参数
//            // 但为了处理 multipart 请求中的非文件参数，我们保留字符串参数
//            if (values != null && values.length > 0) {
//                // 简单判断：如果参数看起来像文件内容（极长字符串），可以截断，但 getParameterMap 只能拿到 String
//                safeParams.put(key, values.length == 1 ? values[0] : values);
//            }
//        });

        // 补充：如果是 multipart 请求，Spring 可能会把文件放在 request attributes 中
        // 我们遍历 attributes 检查是否有 MultipartFile
//        Enumeration<String> attributeNames = request.getAttributeNames();
//        while (attributeNames.hasMoreElements()) {
//            String attrName = attributeNames.nextElement();
//            Object attrValue = request.getAttribute(attrName);
//            if (attrValue instanceof MultipartFile) {
//                // 发现文件，只记录文件名和大小，不记录内容
//                MultipartFile file = (MultipartFile) attrValue;
//                safeParams.put(attrName, String.format("[FILE: %s, size: %d bytes]", file.getOriginalFilename(), file.getSize()));
//            }
//        }
//
//        requestInfo.put("params", safeParams);

        // 4. 打印日志
        try {
            log.info("[HTTP REQUEST:]: {}", JacksonUtil.toJsonString(requestInfo));
        } catch (Exception e) {
            log.warn("Failed to parse request log", e);
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 【核心修改】Response 对象也无法直接序列化，通常只记录状态码
        // 5. 响应日志：只记录状态码，绝对不要尝试读取 response body（流已经被关闭或写入了）
        Map<String, Object> responseInfo = new HashMap<>();
        responseInfo.put("status", response.getStatus());
        responseInfo.put("contentType", response.getContentType());

        try {
            log.info("[HTTP RESPONSE]: {}", JacksonUtil.toJsonString(responseInfo));
        } catch (Exception e) {
            // 忽略序列化异常
        } finally {
            MDC.clear();
        }
        MDC.clear();
    }


    /**
     * 辅助方法：提取所有请求头
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headerMap.put(name, request.getHeader(name));
        }
        return headerMap;
    }
}
