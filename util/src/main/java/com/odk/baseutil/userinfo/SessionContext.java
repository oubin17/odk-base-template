package com.odk.baseutil.userinfo;

import cn.dev33.satoken.stp.StpUtil;

/**
 * SessionContext
 * 用户登录 Session 管理器，核心依赖 SaToken 接口能力
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
     * 从当前登录 session 中获取用户信息
     *
     * @param key
     * @return
     */
    public static Object getSessionValue(String key) {
        return StpUtil.getSession().get(key);
    }

    /**
     * 获取当前用户登录ID，如果用户未登录，抛异常NotLoginException
     *
     * @return
     */
    public static String getLoginIdWithCheck() {
        return StpUtil.getLoginIdAsString();
    }

    /**
     * 返回当前登录ID，如果用户未登录，返回默认值
     *
     * @param defaultValue 默认值
     * @return
     */
    public static String getLoginIdOrDefault(String defaultValue) {
        return isLogin() ? getLoginIdWithCheck() : defaultValue;
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
