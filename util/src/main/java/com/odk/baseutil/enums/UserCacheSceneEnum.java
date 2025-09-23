package com.odk.baseutil.enums;

import com.odk.base.enums.IEnum;

/**
 * UserCacheSceneEnum
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
public enum UserCacheSceneEnum implements IEnum {

    USER_BASIC("basic", "用户信息"),
    USER_PROFILE("profile", "用户信息"),
    USER_ROLE("role", "用户角色"),
    USER_PERMISSION("permission", "用户权限"),
    ;

    private final String code;

    private final String description;

    UserCacheSceneEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 返回缓存 key
     *
     * @param cacheScene
     * @return
     */
    public static String generateCacheKey(UserCacheSceneEnum cacheScene, String key) {
        return "user:" + cacheScene.getCode() + ":info:" + key;
    }

    /**
     * 生成锁 key
     *
     * @param cacheScene
     * @return
     */
    public static String generateLockKey(UserCacheSceneEnum cacheScene, String key) {
        return "user:" + cacheScene.getCode() + ":lock:" + key;
    }

    public static UserCacheSceneEnum getByCode(String code) {
        for (UserCacheSceneEnum value : UserCacheSceneEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
