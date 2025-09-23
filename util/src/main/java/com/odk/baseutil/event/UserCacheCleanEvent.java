package com.odk.baseutil.event;

import com.odk.base.enums.cache.CacheActionEnum;
import com.odk.baseutil.enums.UserCacheSceneEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * UserCacheCleanEvent
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/5
 */
@Data
@AllArgsConstructor
public class UserCacheCleanEvent {

    /**
     * 用户 ID
     */
    private String key;

    /**
     * 更新场景
     */
    private UserCacheSceneEnum cacheScene;

    /**
     * 缓存事件
     */
    private CacheActionEnum action;
}
