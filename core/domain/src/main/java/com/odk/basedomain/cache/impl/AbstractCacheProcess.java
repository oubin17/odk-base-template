package com.odk.basedomain.cache.impl;

import com.odk.basedomain.cache.CacheProcess;
import com.odk.baseutil.enums.UserCacheSceneEnum;
import com.odk.redisspringbootstarter.CacheableDataService;
import com.odk.redisspringbootstarter.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * AbstractCacheProcess
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
@Service
public abstract class AbstractCacheProcess<T> implements CacheProcess<T> {

    protected CacheableDataService cacheableDataService;

    protected RedisUtil redisUtil;

    @Override
    public T getCache(String key) {
        return cacheableDataService.getOrCreate(UserCacheSceneEnum.generateCacheKey(getCacheScene(), key), UserCacheSceneEnum.generateLockKey(getCacheScene(), key), getCacheTimeout(), () -> getDbData(key));
    }

    @Override
    public void evictCache(String key) {
        redisUtil.delete(UserCacheSceneEnum.generateCacheKey(getCacheScene(), key));
    }

    /**
     * 默认缓存过期时间
     *
     * @return
     */
    protected Duration getCacheTimeout() {
        return Duration.ofDays(7);
    }

    @Autowired
    public void setCacheableDataService(CacheableDataService cacheableDataService) {
        this.cacheableDataService = cacheableDataService;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
}
