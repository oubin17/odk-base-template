package com.odk.baseutil.entity;

import com.odk.baseutil.dto.permission.UserRoleDTO;
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
    private String userId;

    /**
     * 角色列表
     */
    private List<UserRoleDTO> roles;
}
