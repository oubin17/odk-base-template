package com.odk.basedomain.cache.impl;

import com.odk.base.util.JacksonUtil;
import com.odk.basedomain.cache.UserCache;
import com.odk.baseutil.constants.UserInfoConstants;
import com.odk.baseutil.entity.UserEntity;
import com.odk.redisspringbootstarter.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * UserCacheImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/5
 */
@Slf4j
@Service
public class UserCacheImpl implements UserCache {

    private RedisUtil redisUtil;

    @Override
    public UserEntity getUserFromCache(String userId) {
        String key = UserInfoConstants.USER_CACHE_KEY_PREFIX + userId;
        try {
            Object obj = redisUtil.get(key);
            if (obj instanceof UserEntity) {
                log.info("缓存获取用户信息，userId={}, userEntity={}", userId,  JacksonUtil.toJsonString(obj));
                return (UserEntity) obj;
            }
        } catch (Exception e) {
            log.error("从缓存获取用户信息失败，userId: {}", userId, e);
        }
        return null;
    }

    @Override
    public void putUserToCache(UserEntity userEntity) {
        String key = UserInfoConstants.USER_CACHE_KEY_PREFIX + userEntity.getUserId();
        try {
            redisUtil.set(key, userEntity, 30, TimeUnit.DAYS);
        } catch (Exception e) {
            log.error("将用户信息存入缓存失败，userEntity: {}", userEntity, e);
        }
    }

    @Override
    public void evictUserFromCache(String userId) {
        String key = UserInfoConstants.USER_CACHE_KEY_PREFIX + userId;
        try {
            redisUtil.delete(key);
        } catch (Exception e) {
            log.error("从缓存删除用户信息失败，userId: {}", userId, e);
        }
    }

    @Override
    public void updateUserInCache(UserEntity userEntity) {
        putUserToCache(userEntity);
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
}
