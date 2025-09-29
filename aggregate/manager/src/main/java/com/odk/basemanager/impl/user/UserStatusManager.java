package com.odk.basemanager.impl.user;

import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.cache.pointcut.UserCacheClean;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.basemanager.api.common.IEventPublish;
import com.odk.basemanager.api.user.IUserStatusManager;
import com.odk.baseutil.enums.UserCacheSceneEnum;
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
    @UserCacheClean(scene = UserCacheSceneEnum.USER_BASIC)
    public Boolean freezeUser(String userId) {
        UserBaseDO userBaseDO = userBaseRepository.findById(userId).orElse(null);
        AssertUtil.notNull(userBaseDO, BizErrorCode.USER_NOT_EXIST);
        userBaseDO.setUserStatus(UserStatusEnum.FROZEN.getCode());
        userBaseRepository.save(userBaseDO);
        SessionContext.kickOut(userId);
        return true;
    }

    @Override
    @UserCacheClean(scene = UserCacheSceneEnum.USER_BASIC)
    public Boolean unfreezeUser(String userId) {
        UserBaseDO userBaseDO = userBaseRepository.findById(userId).orElse(null);
        AssertUtil.notNull(userBaseDO, BizErrorCode.USER_NOT_EXIST);
        userBaseDO.setUserStatus(UserStatusEnum.NORMAL.getCode());
        userBaseRepository.save(userBaseDO);
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
