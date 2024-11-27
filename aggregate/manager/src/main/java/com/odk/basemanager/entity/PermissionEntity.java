package com.odk.basemanager.entity;

import com.odk.basedomain.domain.permission.PermissionDO;
import com.odk.basedomain.domain.permission.UserRoleDO;
import lombok.Data;

import java.util.List;

/**
 * PermissionEntity
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@Data
public class PermissionEntity {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色列表
     */
    private List<UserRoleDO> roles;

    /**
     * 权限列表
     */
    private List<PermissionDO> permissions;
}
