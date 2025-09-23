package com.odk.basedomain.cache.impl;

import com.odk.basedomain.cache.EventPublishListener;
import com.odk.baseutil.enums.UserCacheSceneEnum;
import com.odk.baseutil.event.UserCacheCleanEvent;
import com.odk.redisspringbootstarter.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * EventPublishListenerImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/5
 */
@Slf4j
@Service

public class EventPublishListenerImpl implements EventPublishListener {

    private RedisUtil redisUtil;

    /**
     * 这里不管是什么场景，都直接删除缓存
     * 获取缓存的场景都加了分布式锁，不会有缓存击穿问题
     *
     * @param event
     */
    @EventListener
    @Override
    public void handleEvent(UserCacheCleanEvent event) {
        redisUtil.delete(UserCacheSceneEnum.generateCacheKey(event.getCacheScene(), event.getKey()));
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
}
