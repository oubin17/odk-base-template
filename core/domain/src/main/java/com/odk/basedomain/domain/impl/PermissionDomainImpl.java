package com.odk.basedomain.domain.impl;

import com.google.common.collect.Lists;
import com.odk.base.context.TenantIdContext;
import com.odk.basedomain.dataobject.permission.PermissionDO;
import com.odk.basedomain.dataobject.permission.UserRoleDO;
import com.odk.basedomain.domain.PermissionDomain;
import com.odk.basedomain.mapper.PermissionDomainMapper;
import com.odk.basedomain.mapper.UserDomainMapper;
import com.odk.basedomain.repository.permission.PermissionRepository;
import com.odk.basedomain.repository.permission.UserRoleRepository;
import com.odk.baseutil.dto.permission.PermissionDTO;
import com.odk.baseutil.dto.permission.UserRoleDTO;
import com.odk.baseutil.entity.PermissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PermissionDomainImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/12/4
 */
@Service
public class PermissionDomainImpl implements PermissionDomain {

    private UserRoleRepository userRoleRepository;

    private PermissionRepository permissionRepository;

    private UserDomainMapper userDomainMapper;

    private PermissionDomainMapper permissionDomainMapper;

    @Override
    public PermissionEntity getPermissionByUserId(String userId) {

        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setUserId(userId);
        List<UserRoleDO> userRoleDOS = userRoleRepository.findAllUserRole(userId, TenantIdContext.getTenantId());
        List<UserRoleDTO> userRoleDTOS = Lists.newArrayList();
        for (UserRoleDO userRoleDO : userRoleDOS) {
            UserRoleDTO userRoleDTO = convert(userRoleDO);
            userRoleDTOS.add(userRoleDTO);
            List<PermissionDO> permissionDOS = permissionRepository.findRolePermissionByRoleId(userRoleDTO.getId(), TenantIdContext.getTenantId());
            List<PermissionDTO> permissionDTOS = Lists.newArrayList();
            for (PermissionDO permissionDO : permissionDOS) {
                PermissionDTO permissionDTO = convert(permissionDO);
                permissionDTOS.add(permissionDTO);
            }
            userRoleDTO.setPermissions(permissionDTOS);
        }
        permissionEntity.setRoles(userRoleDTOS);
        return permissionEntity;

    }

    /**
     * DO -> DTO
     *
     * @param userRoleDO
     * @return
     */
    private UserRoleDTO convert(UserRoleDO userRoleDO) {
        return this.userDomainMapper.toDTO(userRoleDO);
    }

    /**
     * DO -> DTO
     *
     * @param permissionDO
     * @return
     */
    private PermissionDTO convert(PermissionDO permissionDO) {
        return this.permissionDomainMapper.toDTO(permissionDO);
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Autowired
    public void setUserDomainMapper(UserDomainMapper userDomainMapper) {
        this.userDomainMapper = userDomainMapper;
    }

    @Autowired
    public void setPermissionDomainMapper(PermissionDomainMapper permissionDomainMapper) {
        this.permissionDomainMapper = permissionDomainMapper;
    }
}
