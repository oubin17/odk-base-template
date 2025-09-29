package com.odk.baseutil.userinfo;

import cn.dev33.satoken.stp.StpUtil;
import com.odk.base.context.TenantIdContext;

/**
 * SessionContext
 * 用户登录 Session 管理器，核心依赖 SaToken 接口能力
 *
 * important: 这里的 loginId，添加了租户信息，以支持多租户，但是对外无感知。数据库存储的 key 样式：userId:tenantId
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/10
 */
public class SessionContext {

    private SessionContext() {}

    /**
     * 获取转换后的登录 id
     *
     * @param loginId
     * @return
     */
    private static String getLoginId(String loginId) {
        return loginId + ":" + TenantIdContext.getTenantId();
    }

    /**
     *
     * important 登录 ID 样式： userId:tenantId
     *
     * 创建登录session，这时候会生成token
     * token格式： key: okd-token:login:token:{token} value: loginId
     *
     * session格式：key: odk-token:login:session:{loginId} value: {@link cn.dev33.satoken.session.SaSession}
     * @param loginId
     */
    public static void createLoginSession(String loginId) {
        StpUtil.login(getLoginId(loginId));
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
     *
     * @return
     */
    public static String getLoginIdWithCheck() {
        return StpUtil.getLoginIdAsString().split(":")[0];
    }

    /**
     * 获取当前登录的用户租户 id
     *
     * @return
     */
    public static String getTenantIdWithCheck() {
        return StpUtil.getLoginIdAsString().split(":")[1];
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

    /**
     * 获取登录 token
     *
     * @return
     */
    public static String getToken() {
        return StpUtil.getTokenInfo().getTokenValue();
    }

    /**
     * 踢人下线
     *
     * @param loginId
     */
    public static void kickOut(String loginId) {
        StpUtil.kickout(getLoginId(loginId));
    }
}
