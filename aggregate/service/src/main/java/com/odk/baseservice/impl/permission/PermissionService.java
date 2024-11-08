package com.odk.baseservice.impl.permission;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.PermissionApi;
import com.odk.baseapi.response.PermissionQueryResponse;
import com.odk.baseapi.vo.PermissionVO;
import com.odk.baseapi.vo.UserRoleVo;
import com.odk.basemanager.deal.permission.PermissionManager;
import com.odk.basemanager.entity.PermissionEntity;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.enums.BizScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PermissionService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/9
 */
@Service
public class PermissionService extends AbstractApiImpl implements PermissionApi {

    private PermissionManager permissionManager;


    @Override
    public ServiceResponse<PermissionQueryResponse> userPermission(String userId) {
        return super.queryProcess(BizScene.USER_PERMISSION, userId, new QueryApiCallBack<PermissionEntity, PermissionQueryResponse>() {

            @Override
            protected void checkParams(Object request) {
                AssertUtil.notNull(request, BizErrorCode.PARAM_ILLEGAL, "userId is null.");
            }

            @Override
            protected PermissionEntity doProcess(Object args) {
                return permissionManager.getAllPermissions(userId);
            }

            @Override
            protected PermissionQueryResponse convertResult(PermissionEntity permissionEntity) {
                if (null == permissionEntity) {
                    return null;
                }
                PermissionQueryResponse permissionQueryResponse = new PermissionQueryResponse();
                permissionQueryResponse.setUserId(permissionEntity.getUserId());
                List<UserRoleVo> roles = permissionEntity.getRoles().stream().map(userRoleDO -> {
                    UserRoleVo userRoleVo = new UserRoleVo();
                    userRoleVo.setRoleId(userRoleDO.getRoleId());
                    userRoleVo.setRoleCode(userRoleDO.getRoleCode());
                    userRoleVo.setRoleName(userRoleDO.getRoleName());
                    userRoleVo.setStatus(userRoleDO.getStatus());
                    return userRoleVo;
                }).collect(Collectors.toList());
                permissionQueryResponse.setRoles(roles);
                List<PermissionVO> permissions = permissionEntity.getPermissions().stream().map(permissionDO -> {
                    PermissionVO permissionVO = new PermissionVO();
                    permissionVO.setPermissionId(permissionDO.getPermissionId());
                    permissionVO.setPermissionCode(permissionDO.getPermissionCode());
                    permissionVO.setPermissionName(permissionDO.getPermissionName());
                    permissionVO.setStatus(permissionDO.getStatus());
                    return permissionVO;
                }).collect(Collectors.toList());
                permissionQueryResponse.setPermissions(permissions);
                return permissionQueryResponse;
            }

        });
    }

    @Autowired
    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }
}
