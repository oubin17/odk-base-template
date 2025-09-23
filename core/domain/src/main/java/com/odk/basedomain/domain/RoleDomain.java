package com.odk.basedomain.domain;

import com.odk.baseutil.entity.RoleEntity;

import java.util.List;

/**
 * RoleDomain
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/23
 */
public interface RoleDomain {


    /**
     * 根据用户 id 查找角色（不包含权限）
     *
     * @param userId
     * @return
     */
    List<RoleEntity> getRoleListByUserId(String userId);
}
