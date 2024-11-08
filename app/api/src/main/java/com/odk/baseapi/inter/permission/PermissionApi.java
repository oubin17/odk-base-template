package com.odk.baseapi.inter.permission;

import com.odk.base.vo.response.ServiceResponse;
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

}
