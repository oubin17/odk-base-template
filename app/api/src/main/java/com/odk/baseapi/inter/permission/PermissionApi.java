package com.odk.baseapi.inter.permission;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.request.role.RoleAddRequest;
import com.odk.baseapi.request.role.UserRoleRelaRequest;
import com.odk.baseapi.response.PermissionQueryResponse;

/**
 * PermissionApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
public interface PermissionApi {


    /**
     * 用户权限
     *
     * @param userId
     * @return
     */
    ServiceResponse<PermissionQueryResponse> userPermission(String userId);


    /**
     * 添加角色
     *
     * @param roleAddRequest
     * @return
     */
    ServiceResponse<String> addRole(RoleAddRequest roleAddRequest);

    /**
     * 添加用户角色
     *
     * @param relaRequest
     * @return
     */
    ServiceResponse<String> addRoleRela(UserRoleRelaRequest relaRequest);

}
