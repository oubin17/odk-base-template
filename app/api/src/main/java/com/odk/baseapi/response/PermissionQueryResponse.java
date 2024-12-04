package com.odk.baseapi.response;

import com.odk.baseutil.dto.permission.PermissionDTO;
import com.odk.baseutil.dto.permission.UserRoleDTO;
import lombok.Data;

import java.util.List;

/**
 * PermissionQueryResponse
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@Data
public class PermissionQueryResponse {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色列表
     */
    private List<UserRoleDTO> roles;

    /**
     * 所有权限集合
     */
    private List<PermissionDTO> permissions;
}
