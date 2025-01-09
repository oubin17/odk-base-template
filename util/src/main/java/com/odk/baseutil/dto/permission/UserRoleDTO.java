package com.odk.baseutil.dto.permission;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * UserRoleVo
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/9
 */
@Data
public class UserRoleDTO {

    /**
     * 角色id
     */
    private String id;

    /**
     * 角色码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色状态
     */
    private String status;

    /**
     * 角色对应权限
     */
    private List<PermissionDTO> permissions = Lists.newArrayList();
}
