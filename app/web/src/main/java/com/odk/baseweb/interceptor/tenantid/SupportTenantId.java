package com.odk.baseweb.interceptor.tenantid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SupportTenant
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportTenantId {

    /**
     * 默认租户ID
     * {@link com.odk.base.context.TenantIdContext}
     * @return
     */
    String value() default "";
}
