package com.odk.basedomain.domain.impl;

import com.odk.base.context.TenantIdContext;
import com.odk.basedomain.cache.impl.AbstractCacheProcess;
import com.odk.basedomain.domain.RoleDomain;
import com.odk.basedomain.convert.UserDomainMapper;
import com.odk.basedomain.model.permission.UserRoleDO;
import com.odk.basedomain.repository.permission.UserRoleRepository;
import com.odk.baseutil.entity.RoleEntity;
import com.odk.baseutil.enums.UserCacheSceneEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RoleDomainImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
@Service
public class RoleDomainImpl extends AbstractCacheProcess<RoleEntity> implements RoleDomain {

    private UserRoleRepository userRoleRepository;

    private UserDomainMapper userDomainMapper;

    @Override
    public RoleEntity getDbData(String key) {
        UserRoleDO roleDO = userRoleRepository.findById(key).orElse(null);
        return userDomainMapper.toEntity(roleDO);
    }

    @Override
    public UserCacheSceneEnum getCacheScene() {
        return UserCacheSceneEnum.USER_ROLE;
    }



    @Override
    public List<RoleEntity> getRoleListByUserId(String userId) {
        List<UserRoleDO> userRoleDOS = userRoleRepository.findAllUserRole(userId, TenantIdContext.getTenantId());
        return userRoleDOS.stream().map(userRoleDO -> getCache(userRoleDO.getId())).collect(Collectors.toList());
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setUserDomainMapper(UserDomainMapper userDomainMapper) {
        this.userDomainMapper = userDomainMapper;
    }

}
