package com.odk.basemanager.impl.common;

import com.odk.basemanager.api.common.IEventPublish;
import com.odk.baseutil.event.UserCacheCleanEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * EventPublish
 * 默认同步阻塞
 * 线程间互不影响
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/5
 */
@Service
public class EventPublish implements IEventPublish {

    private final ApplicationEventPublisher eventPublisher;

    public EventPublish(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(UserCacheCleanEvent event) {
        eventPublisher.publishEvent(event);
    }
}
