package com.odk.basedomain.cache;

import com.odk.baseutil.enums.UserCacheSceneEnum;

/**
 * CacheManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
public interface CacheProcess<T> {

    /**
     * 获取缓存数据
     *
     * @param key
     * @return
     */
    T getCache(String key);

    /**
     * 当缓存未空时，获取数据库数据
     *
     * @param key
     * @return
     */
    T getDbData(String key);

    /**
     * 缓存场景
     *
     * @return
     */
    UserCacheSceneEnum getCacheScene();


}
