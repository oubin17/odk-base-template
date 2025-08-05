package com.odk.basedomain.cache;

import com.odk.baseutil.event.UserCacheCleanEvent;

/**
 * EventPublishListener
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/5
 */
public interface EventPublishListener {

    /**
     * 用户缓存更新事件
     *
     * 阻塞式：发布事件的线程会被阻塞，直到所有监听器执行完成
     * 顺序执行：监听器按照注册顺序依次执行
     * 事务感知：可以与Spring事务集成（使用@TransactionalEventListener）
     * 异常传播：监听器中的异常会传播回调用方
     *
     * 数据一致性：缓存清理和预热操作完成后才返回，确保数据一致性
     * 无需担心竞态条件：发布线程会等待事件处理完成
     *
     * @param event
     */
    void handleEvent(UserCacheCleanEvent event);
}
