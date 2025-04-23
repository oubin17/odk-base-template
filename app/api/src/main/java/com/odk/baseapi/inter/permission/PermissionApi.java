package com.odk.baseapi.inter.permission;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.dto.permission.PermissionDTO;
import com.odk.baseutil.request.role.PermissionAddRequest;
import com.odk.baseutil.request.role.PermissionQueryRequest;

import java.util.List;

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

    /**
     * 权限列表
     *
     * @param permissionQueryRequest
     * @return
     */
    ServiceResponse<List<PermissionDTO>> permissionList(PermissionQueryRequest permissionQueryRequest);

}
