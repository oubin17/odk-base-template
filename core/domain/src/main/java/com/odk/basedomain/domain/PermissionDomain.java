package com.odk.basedomain.domain;

import com.odk.baseutil.entity.PermissionEntity;
import com.odk.baseutil.entity.RoleEntity;

import java.util.List;

/**
 * PermissionDomain
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/12/4
 */
public interface PermissionDomain {

    /**
     * 根据用户ID查找权限
     *
     * @param userId
     * @return
     */
    PermissionEntity getPermissionByUserId(String userId);

    /**
     * 根据用户 id 查找角色（不包含权限）
     *
     * @param userId
     * @return
     */
    List<RoleEntity> getRoleListByUserId(String userId);
}
