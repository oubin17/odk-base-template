package com.odk.baseweb.interceptor.tenantid;

import com.odk.base.context.TenantIdContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * TenantIdInterceptor
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/7/23
 */
public class TenantIdInterceptor implements HandlerInterceptor {

    private static final String HEADER_TRACE_ID = "X-TENANT-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantId = request.getHeader(HEADER_TRACE_ID);
        TenantIdContext.setTenantId(tenantId);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TenantIdContext.clear();
    }
}
