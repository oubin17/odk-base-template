package com.odk.baseutil.context;


import com.odk.baseutil.constants.ServiceConstants;
import com.odk.baseutil.enums.BizScene;

/**
 * ServiceContextHolder
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2023/11/11
 */
public class ServiceContextHolder {

    private static final ThreadLocal<BizScene> sceneCode = new ThreadLocal<>();

    private static final ThreadLocal<ServiceContext> SERVICE_CONTEXT = new ThreadLocal<>();

    private static final ThreadLocal<String> USER_ID_CONTEXT = new ThreadLocal<>();


    /**
     * 设置场景上下文
     *
     * @param bizScene
     */
    public static void setSceneCode(BizScene bizScene) {
        ServiceContextHolder.sceneCode.set(bizScene);
    }

    public static void setUserId(String userId) {
        ServiceContext context = SERVICE_CONTEXT.get();
        if (null == context) {
            context = new ServiceContext();
            SERVICE_CONTEXT.set(context);
        }
        context.setUserId(userId);
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public static String getUserId() {
        ServiceContext sc = SERVICE_CONTEXT.get();
        if (null == sc) {
            return null;
        }
        return sc.getUserId();
    }

    /**
     * 获取租户ID
     *
     * @return
     */
    public static String getTntInstId() {
        ServiceContext sc = SERVICE_CONTEXT.get();
        if (null == sc) {
            return ServiceConstants.TENANT_ID;
        }
        return sc.getTenantId();
    }

    public static void clear() {
        sceneCode.remove();
        SERVICE_CONTEXT.remove();
        USER_ID_CONTEXT.remove();
    }

}
