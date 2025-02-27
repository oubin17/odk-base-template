package com.odk.baseapi.inter.permission;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.request.role.PermissionAddRequest;

/**
 * PermissionApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
public interface PermissionApi {

    /**
     * 添加权限
     *
     * @param permissionAddRequest
     * @return
     */
    ServiceResponse<Boolean> addPermission(PermissionAddRequest permissionAddRequest);


}
