package com.odk.basedomain.domain.impl;

import com.odk.base.context.TenantIdContext;
import com.odk.basedomain.cache.impl.AbstractCacheProcess;
import com.odk.basedomain.convert.UserDomainConvert;
import com.odk.basedomain.domain.UserProfileDomain;
import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.basedomain.repository.user.UserProfileRepository;
import com.odk.baseutil.entity.UserProfileEntity;
import com.odk.baseutil.enums.UserCacheSceneEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserProfileDomainImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
@Slf4j
@Service
public class UserProfileDomainImpl extends AbstractCacheProcess<UserProfileEntity> implements UserProfileDomain {

    private UserProfileRepository userProfileRepository;

    private UserDomainConvert userDomainConvert;

    @Override
    public UserProfileEntity getDbData(String key) {
        UserProfileDO userProfileDO = userProfileRepository.findByUserIdAndTenantId(key, TenantIdContext.getTenantId());
        if (null != userProfileDO) {
            return this.userDomainConvert.toEntity(userProfileDO);
        }
        return null;
    }

    @Override
    public UserCacheSceneEnum getCacheScene() {
        return UserCacheSceneEnum.USER_PROFILE;
    }

    @Override
    public UserProfileEntity getUserProfileByUserId(String userId) {
        return getCache(userId);
    }

    @Autowired
    public void setUserProfileRepository(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Autowired
    public void setUserDomainMapper(UserDomainConvert userDomainConvert) {
        this.userDomainConvert = userDomainConvert;
    }
}
