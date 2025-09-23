package com.odk.baseutil.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * RoleEntity
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
@Data
public class RoleEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3181791154937333521L;

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
}
