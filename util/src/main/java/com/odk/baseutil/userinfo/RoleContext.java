package com.odk.baseutil.userinfo;

import cn.dev33.satoken.stp.StpUtil;

/**
 * RoleContext
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/10
 */
public class RoleContext {

    /**
     * 检查当前登录用户是否具备 role 权限，需要配合拦截器实现
     *
     * {@link com.odk.baseweb.interceptor.auth.PermissionCheckInterfaceImpl}
     * @param role
     */
    public static void checkRole(String role) {
        StpUtil.checkRole(role);
    }
}
