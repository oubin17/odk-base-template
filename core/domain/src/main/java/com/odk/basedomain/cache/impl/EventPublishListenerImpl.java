package com.odk.basedomain.cache.impl;

import com.odk.basedomain.cache.EventPublishListener;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.baseutil.constants.UserInfoConstants;
import com.odk.baseutil.enums.UserQueryTypeEnum;
import com.odk.baseutil.event.UserCacheCleanEvent;
import com.odk.redisspringbootstarter.RedisUtil;
import lombok.extern.slf4j.Slf4j;
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

    private final RedisUtil redisUtil;
    private final UserQueryDomain userQueryDomain;

    public EventPublishListenerImpl(RedisUtil redisUtil, UserQueryDomain userQueryDomain) {
        this.redisUtil = redisUtil;
        this.userQueryDomain = userQueryDomain;
    }

    @EventListener
    @Override
    public void handleEvent(UserCacheCleanEvent event) {
        redisUtil.delete(UserInfoConstants.USER_CACHE_KEY_PREFIX + event.getUserId());
        switch (event.getAction()) {
            case ADD, UPDATE -> userQueryDomain.queryUser(UserQueryCriteria.builder().queryType(UserQueryTypeEnum.USER_ID).userId(event.getUserId()).build());
            case DELETE -> {
            }
        }
    }
}
