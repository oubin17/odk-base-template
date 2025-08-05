package com.odk.basemanager.api.common;

import com.odk.baseutil.event.UserCacheCleanEvent;

/**
 * EventPublish
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/5
 */
public interface IEventPublish {

    /**
     * 用户缓存清空动作
     *
     * @param event
     */
    void publish(UserCacheCleanEvent event);
}
