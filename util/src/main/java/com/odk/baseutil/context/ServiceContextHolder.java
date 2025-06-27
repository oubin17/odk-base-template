package com.odk.baseutil.context;


import com.odk.base.util.JacksonUtil;
import com.odk.baseutil.enums.BizScene;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * ServiceContextHolder
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2023/11/11
 */
@Slf4j
public class ServiceContextHolder {

    /**
     * 场景上下文
     */
    private static final ThreadLocal<BizScene> SCENE_CODE = new ThreadLocal<>();

    /**
     * 服务上下文，不限制格式
     */
    private static final ThreadLocal<Object> SERVICE_CONTEXT = new ThreadLocal<>();

    /**
     * 报错上下文，需要是json格式
     */
    private static final ThreadLocal<Map<String, Object>> ERROR_CONTEXT = new ThreadLocal<>();

    /**
     * 设置场景上下文
     *
     * @param bizScene
     */
    public static void setSceneCode(BizScene bizScene) {
        ServiceContextHolder.SCENE_CODE.set(bizScene);
    }

    /**
     * 获取场景上下文
     *
     * @return
     */
    public static BizScene getSceneCode() {
        return ServiceContextHolder.SCENE_CODE.get();
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

    /**
     * 设置报错上下文，需要保证对象是json格式
     *
     * @param obj
     */
    public static void setErrorContext(Object obj) {
        String jsonString = JacksonUtil.toJsonString(obj);
        try {
            ERROR_CONTEXT.set(JacksonUtil.parseObject(jsonString, Map.class));
        } catch (Exception e) {
            log.error("对象转 json 报错:", e);
        }
    }

    /**
     * 获取报错上下文
     *
     * @return
     */
    public static Map<String, Object> getErrorContext() {
        return ERROR_CONTEXT.get();
    }

    /**
     * 清空上下文
     */
    public static void clear() {
        SCENE_CODE.remove();
        SERVICE_CONTEXT.remove();
        ERROR_CONTEXT.remove();
    }

}
