package com.odk.baseweb.interceptor.tenantid;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizException;
import com.odk.baseutil.userinfo.SessionContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.odk.base.exception.BizErrorCode.SYSTEM_ERROR;
import static com.odk.base.exception.BizErrorCode.TENANT_NOT_MATCH;

/**
 * SupportTenantIdInterceptor
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/18
 */
public class SupportTenantIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只处理方法级别的处理器
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Class<?> controllerClass = handlerMethod.getBeanType();

        // 检查控制器类是否有 TenantSupport 注解
        if (controllerClass.isAnnotationPresent(SupportTenantId.class)) {
            SupportTenantId tenantSupport = controllerClass.getAnnotation(SupportTenantId.class);
            String expectedTenant = tenantSupport.value();

            //如果注解租户为空，不允许访问
            if (StringUtils.isEmpty(expectedTenant)) {
                throw new BizException(SYSTEM_ERROR, "系统异常，请设置租户信息");
            }

            // 从请求头获取租户信息，这里不根据 header X-TENANT-ID 判断，而是根据实际的 token 获取租户信息。
            String requestTenant = SessionContext.getTenantIdWithCheck();

            // 如果注解指定了特定租户，需要验证是否匹配
            AssertUtil.equal(expectedTenant, requestTenant, TENANT_NOT_MATCH, "租户不匹配");
//            if (!expectedTenant.equals(requestTenant)) {
//                throw new BizException(TENANT_NOT_MATCH, "租户不匹配，非法访问");
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.getWriter().write("{\"error\":\"Tenant mismatch: expected " + expectedTenant + ", but got " + requestTenant + "\"}");
//                return false;
//            }

            // 如果注解存在但未指定具体租户，则只需验证请求头中存在租户信息
//            if (expectedTenant.isEmpty() && (requestTenant == null || requestTenant.isEmpty())) {
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                response.getWriter().write("{\"error\":\"Missing tenant information in request header\"}");
//                return false;
//            }
        }

        return true;
    }
}
