package com.odk.baseutil.dto.permission;

import lombok.Data;

/**
 * PermissionVO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/9
 */
@Data
public class PermissionDTO {

    /**
     * 权限ID
     */
    private String id;

    /**
     * 权限码
     */
    private String permissionCode;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限状态
     */
    private String status;
}
