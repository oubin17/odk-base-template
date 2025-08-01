package com.odk.basemanager.api.permission;

import com.odk.baseutil.dto.permission.UserRoleDTO;
import com.odk.baseutil.entity.PermissionEntity;

import java.util.List;

/**
 * IRoleManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/1
 */
public interface IRoleManager {

    /**
     * 查找用户权限
     *
     * @param userId
     * @return
     */
    PermissionEntity getAllPermissions(String userId);

    /**
     * 添加角色
     *
     * @param roleCode
     * @param roleName
     * @return
     */
    String addRole(String roleCode, String roleName);

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    Boolean deleteRole(String roleId);

    /**
     * 角色列表
     *
     * @return
     */
    List<UserRoleDTO> roleList();

    /**
     * 添加角色关系
     * @param roleId
     * @param userId
     * @return
     */
    String addUserRoleRela(String roleId, String userId);

    /**
     * 删除角色关系
     *
     * @param roleId
     * @param userId
     * @return
     */
    Boolean deleteUserRoleRela(String roleId, String userId);
}
