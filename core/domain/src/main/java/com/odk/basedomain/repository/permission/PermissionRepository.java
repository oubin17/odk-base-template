package com.odk.basedomain.repository.permission;

import com.odk.basedomain.model.permission.PermissionDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * PermissionRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
public interface PermissionRepository extends JpaRepository<PermissionDO, String> {

    @Query(value = "select * from t_user_permission where id in (select permission_id from t_role_permission_rel where role_id in (:roleIds)) and tenant_id = :tenantId and status = '0'", nativeQuery = true)
    List<PermissionDO> findAllRolePermission(@Param("roleIds") List<String> roleIds, @Param("tenantId") String tenantId);

    /**
     * 根据角色id查找角色对应权限
     *
     * @param roleId
     * @param tenantId
     * @return
     */
    @Query(value = "select * from t_user_permission where id in (select permission_id from t_role_permission_rel where role_id = :roleId and tenant_id = :tenantId) and status = '0'", nativeQuery = true)
    List<PermissionDO> findRolePermissionByRoleId(@Param("roleId") String roleId, @Param("tenantId") String tenantId);

}
