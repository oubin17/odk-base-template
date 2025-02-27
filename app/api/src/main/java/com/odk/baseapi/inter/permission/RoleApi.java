package com.odk.baseapi.inter.permission;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.request.role.RoleAddRequest;
import com.odk.baseutil.request.role.UserRoleRelaRequest;
import com.odk.baseutil.response.PermissionQueryResponse;

/**
 * RoleApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/2/27
 */
public interface RoleApi {

    /**
     * 用户权限
     *
     * @param userId
     * @return
     */
    ServiceResponse<PermissionQueryResponse> userRoles(String userId);

    /**
     * 添加角色
     *
     * @param roleAddRequest
     * @return
     */
    ServiceResponse<String> addRole(RoleAddRequest roleAddRequest);

    /**
     * 添加角色
     *
     * @param roleId
     * @return
     */
    ServiceResponse<Boolean> deleteRole(String roleId);

    /**
     * 添加用户角色
     *
     * @param relaRequest
     * @return
     */
    ServiceResponse<String> addRoleRela(UserRoleRelaRequest relaRequest);

}
