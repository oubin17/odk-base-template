package com.odk.basedomain.domain.impl;

import com.odk.base.context.TenantIdContext;
import com.odk.basedomain.cache.impl.AbstractCacheProcess;
import com.odk.basedomain.domain.AccessTokenDomain;
import com.odk.basedomain.mapper.UserDomainMapper;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.baseutil.entity.AccessTokenEntity;
import com.odk.baseutil.enums.UserCacheSceneEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AccessTokenDomainImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
@Service
public class AccessTokenDomainImpl extends AbstractCacheProcess<AccessTokenEntity> implements AccessTokenDomain {

    private UserAccessTokenRepository accessTokenRepository;

    private UserDomainMapper userDomainMapper;

    @Override
    public AccessTokenEntity getDbData(String key) {
        // 查询并设置账号信息
        UserAccessTokenDO accessTokenDO = accessTokenRepository.findByUserIdAndTenantId(key, TenantIdContext.getTenantId());
        return this.userDomainMapper.toEntity(accessTokenDO);
    }

    @Override
    public UserCacheSceneEnum getCacheScene() {
        return UserCacheSceneEnum.USER_ACCESS_TOKEN;
    }

    @Override
    public AccessTokenEntity getAccessToken(String userId) {
        return getCache(userId);
    }

    @Autowired
    public void setAccessTokenRepository(UserAccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Autowired
    public void setUserDomainMapper(UserDomainMapper userDomainMapper) {
        this.userDomainMapper = userDomainMapper;
    }
}
