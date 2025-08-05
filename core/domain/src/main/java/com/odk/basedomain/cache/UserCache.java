package com.odk.basedomain.cache;

import com.odk.baseutil.entity.UserEntity;

/**
 * UserCache
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/5
 */
public interface UserCache {
    /**
     * 根据用户ID从缓存中获取用户信息，缓存为空返回空值
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserEntity getUserFromCache(String userId);

    /**
     * 将用户信息存入缓存
     *
     * @param userEntity 用户信息
     */
    void putUserToCache(UserEntity userEntity);

    /**
     * 从缓存中删除用户信息
     *
     * @param userId 用户ID
     */
    void evictUserFromCache(String userId);

    /**
     * 更新缓存中的用户信息
     *
     * @param userEntity 用户信息
     */
    void updateUserInCache(UserEntity userEntity);
}
