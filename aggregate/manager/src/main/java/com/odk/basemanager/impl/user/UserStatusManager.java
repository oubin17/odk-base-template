package com.odk.basemanager.impl.user;

import com.odk.base.enums.cache.CacheActionEnum;
import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.basemanager.api.common.IEventPublish;
import com.odk.basemanager.api.user.IUserStatusManager;
import com.odk.baseutil.enums.UserCacheSceneEnum;
import com.odk.baseutil.event.UserCacheCleanEvent;
import com.odk.baseutil.userinfo.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserStatusManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/29
 */
@Service
public class UserStatusManager implements IUserStatusManager {

    private UserBaseRepository userBaseRepository;

    private IEventPublish eventPublish;

    @Override
    public Boolean freezeUser(String userId) {
        UserBaseDO userBaseDO = userBaseRepository.findById(userId).orElse(null);
        AssertUtil.notNull(userBaseDO, BizErrorCode.USER_NOT_EXIST);
        userBaseDO.setUserStatus(UserStatusEnum.FROZEN.getCode());
        userBaseRepository.save(userBaseDO);
        SessionContext.logOut();
        eventPublish.publish(new UserCacheCleanEvent(userId, UserCacheSceneEnum.USER_BASIC, CacheActionEnum.DELETE));
        return true;
    }

    @Override
    public Boolean unfreezeUser(String userId) {
        UserBaseDO userBaseDO = userBaseRepository.findById(userId).orElse(null);
        AssertUtil.notNull(userBaseDO, BizErrorCode.USER_NOT_EXIST);
        userBaseDO.setUserStatus(UserStatusEnum.NORMAL.getCode());
        userBaseRepository.save(userBaseDO);
        eventPublish.publish(new UserCacheCleanEvent(userId, UserCacheSceneEnum.USER_BASIC, CacheActionEnum.DELETE));
        return true;
    }

    @Autowired
    public void setUserBaseRepository(UserBaseRepository userBaseRepository) {
        this.userBaseRepository = userBaseRepository;
    }

    @Autowired
    public void setEventPublish(IEventPublish eventPublish) {
        this.eventPublish = eventPublish;
    }
}
