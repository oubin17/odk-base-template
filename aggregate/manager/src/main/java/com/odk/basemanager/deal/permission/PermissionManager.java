package com.odk.basemanager.deal.permission;

import com.odk.basedomain.domain.permission.PermissionDO;
import com.odk.basedomain.domain.permission.UserRoleDO;
import com.odk.basedomain.repository.permission.PermissionRepository;
import com.odk.basedomain.repository.permission.UserRoleRepository;
import com.odk.basemanager.entity.PermissionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PermissionManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@Slf4j
@Service
public class PermissionManager {

    private UserRoleRepository userRoleRepository;

    private PermissionRepository permissionRepository;

    /**
     * 查找用户权限
     *
     * @param userId
     * @return
     */
    public PermissionEntity getAllPermissions(String userId) {
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setUserId(userId);
        List<UserRoleDO> userRoleDOS = userRoleRepository.findAllUserRole(userId);
        permissionEntity.setRoles(userRoleDOS);
        if (!CollectionUtils.isEmpty(userRoleDOS)) {
            List<String> roleIds = userRoleDOS.stream().map(UserRoleDO::getRoleId).collect(Collectors.toList());
            List<PermissionDO> allRolePermission = permissionRepository.findAllRolePermission(roleIds);
            permissionEntity.setPermissions(allRolePermission);
        }
        return permissionEntity;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
}
