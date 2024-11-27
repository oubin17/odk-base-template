package com.odk.baseapi.vo;

import lombok.Data;

/**
 * UserRoleVo
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/9
 */
@Data
public class UserRoleVo {

    /**
     * 角色id
     */
    private Long id;

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
}
