package com.odk.baseutil.userinfo;

import cn.dev33.satoken.stp.StpUtil;

/**
 * SessionContext
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/10
 */
public class SessionContext {

    private SessionContext() {}

    /**
     * 创建登录session，这时候会生成token
     * token格式： key: okd-token:login:token:{token} value: loginId
     *
     * session格式：key: odk-token:login:session:{loginId} value: {@link cn.dev33.satoken.session.SaSession}
     * @param loginId
     */
    public static void createLoginSession(Object loginId) {
        StpUtil.login(loginId);
    }

    /**
     * 设置key value键值对到当前的登录session中，如当前用户信息
     *
     * @param key
     * @param value
     */
    public static void setSessionValue(String key, Object value) {
        StpUtil.getSession().set(key, value);
    }

    /**
     * 获取当前用户登录ID
     *
     * @return
     */
    public static String getLoginIdAsString() {
        return StpUtil.getLoginIdAsString();
    }

    /**
     * 登出：清除session
     */
    public static void logOut() {
        StpUtil.logout();
    }

    /**
     * 判断是否是登录态
     *
     * @return
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }

    /**
     * 检查是否是登录态，即token是否生效，如果不生效，抛异常
     */
    public static void checkLogin() {
        StpUtil.checkLogin();
    }
}
