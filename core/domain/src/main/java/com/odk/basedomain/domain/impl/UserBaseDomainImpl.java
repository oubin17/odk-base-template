package com.odk.basedomain.domain.impl;

import com.odk.base.context.TenantIdContext;
import com.odk.basedomain.cache.impl.AbstractCacheProcess;
import com.odk.basedomain.domain.UserBaseDomain;
import com.odk.basedomain.convert.UserDomainConvert;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.baseutil.entity.UserBaseEntity;
import com.odk.baseutil.enums.UserCacheSceneEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserBaseDomainImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
@Slf4j
@Service
public class UserBaseDomainImpl extends AbstractCacheProcess<UserBaseEntity> implements UserBaseDomain {

    private UserBaseRepository userBaseRepository;

    private UserDomainConvert userDomainConvert;

    @Override
    public UserBaseEntity getDbData(String key) {
        Optional<UserBaseDO> userBaseDOOptional = userBaseRepository.findByIdAndTenantId(key, TenantIdContext.getTenantId());
        if (userBaseDOOptional.isEmpty()) {
            log.error("找不到用户，用户ID={}", key);
            return null;
        }
        return userDomainConvert.toEntity(userBaseDOOptional.get());
    }

    @Override
    public UserCacheSceneEnum getCacheScene() {
        return UserCacheSceneEnum.USER_BASIC;
    }

    @Override
    public UserBaseEntity getUserBaseInfo(String userId) {
        return getCache(userId);
    }

    @Autowired
    public void setUserBaseRepository(UserBaseRepository userBaseRepository) {
        this.userBaseRepository = userBaseRepository;
    }

    @Autowired
    public void setUserDomainMapper(UserDomainConvert userDomainConvert) {
        this.userDomainConvert = userDomainConvert;
    }
}
