package com.odk.basemanager.api.permission;

import com.odk.baseutil.dto.permission.PermissionDTO;
import com.odk.baseutil.request.role.PermissionAddRequest;
import com.odk.baseutil.request.role.PermissionQueryRequest;

import java.util.List;

/**
 * IPermissionManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/1
 */
public interface IPermissionManager {

    /**
     * 权限列表
     *
     * @param queryRequest
     * @return
     */
    List<PermissionDTO> permissionList(PermissionQueryRequest queryRequest);

    /**
     * 添加权限
     *
     * @param permissionAddRequest
     * @return
     */
    Boolean addPermission(PermissionAddRequest permissionAddRequest);
}
