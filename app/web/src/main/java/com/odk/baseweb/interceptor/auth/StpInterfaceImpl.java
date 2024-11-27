package com.odk.baseweb.interceptor.auth;

import cn.dev33.satoken.stp.StpInterface;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.PermissionApi;
import com.odk.baseapi.response.PermissionQueryResponse;
import com.odk.baseapi.vo.PermissionVO;
import com.odk.baseapi.vo.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * StpInterfaceImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/26
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    private PermissionApi permissionApi;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        ServiceResponse<PermissionQueryResponse> response = permissionApi.userPermission((Long) loginId);
        List<PermissionVO> permissions = response.getData().getPermissions();
        return permissions.stream().map(PermissionVO::getPermissionCode).collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        ServiceResponse<PermissionQueryResponse> response = permissionApi.userPermission((Long) loginId);
        List<UserRoleVo> roles = response.getData().getRoles();
        return roles.stream().map(UserRoleVo::getRoleCode).collect(Collectors.toList());
    }

    @Autowired
    public void setPermissionApi(PermissionApi permissionApi) {
        this.permissionApi = permissionApi;
    }
}
