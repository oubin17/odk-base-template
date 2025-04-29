package com.odk.baseutil.context;


import com.odk.baseutil.enums.BizScene;

/**
 * ServiceContextHolder
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2023/11/11
 */
public class ServiceContextHolder {

    /**
     * 场景上下文
     */
    private static final ThreadLocal<BizScene> SCENE_CODE = new ThreadLocal<>();

    /**
     * 服务上下文，不限制格式
     */
    private static final ThreadLocal<Object> SERVICE_CONTEXT = new ThreadLocal<>();

    private static final ThreadLocal<String> USER_ID_CONTEXT = new ThreadLocal<>();


    /**
     * 设置场景上下文
     *
     * @param bizScene
     */
    public static void setSceneCode(BizScene bizScene) {
        ServiceContextHolder.SCENE_CODE.set(bizScene);
    }

    /**
     * 设置上下文
     *
     * @param object
     */
    public static void setServiceContext(Object object) {
        ServiceContextHolder.SERVICE_CONTEXT.set(object);
    }

    /**
     * 返回上下文
     *
     * @return
     */
    public static Object getServiceContext() {
        return SERVICE_CONTEXT.get();
    }

//    public static void setUserId(String userId) {
//        ServiceContext context = SERVICE_CONTEXT.get();
//        if (null == context) {
//            context = new ServiceContext();
//            SERVICE_CONTEXT.set(context);
//        }
//        context.setUserId(userId);
//    }

    /**
     * 获取用户ID
     *
     * @return
     */
//    public static String getUserId() {
//        ServiceContext sc = SERVICE_CONTEXT.get();
//        if (null == sc) {
//            return null;
//        }
//        return sc.getUserId();
//    }

    /**
     * 获取租户ID
     *
     * @return
     */
//    public static String getTntInstId() {
//        ServiceContext sc = SERVICE_CONTEXT.get();
//        if (null == sc) {
//            return ServiceConstants.TENANT_ID;
//        }
//        return sc.getTenantId();
//    }

    public static void clear() {
        SCENE_CODE.remove();
        SERVICE_CONTEXT.remove();
        USER_ID_CONTEXT.remove();
    }

}
