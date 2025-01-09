package com.odk.baseservice.impl.permission;

import cn.dev33.satoken.stp.StpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.PermissionApi;
import com.odk.baseapi.request.role.RoleAddRequest;
import com.odk.baseapi.request.role.UserRoleRelaRequest;
import com.odk.baseapi.response.PermissionQueryResponse;
import com.odk.basedomain.entity.PermissionEntity;
import com.odk.basemanager.deal.permission.PermissionManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.dto.permission.PermissionDTO;
import com.odk.baseutil.dto.permission.UserRoleDTO;
import com.odk.baseutil.enums.BizScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        return super.queryProcess(BizScene.USER_PERMISSION_QUERY, userId, new QueryApiCallBack<PermissionEntity, PermissionQueryResponse>() {

            @Override
            protected Object convert(Object request) {

                return Objects.requireNonNullElseGet(request, StpUtil::getLoginIdAsLong);
            }

            @Override
            protected PermissionEntity doProcess(Object args) {
                return permissionManager.getAllPermissions((String) args);
            }

            @Override
            protected PermissionQueryResponse convertResult(PermissionEntity permissionEntity) {
                if (null == permissionEntity) {
                    return null;
                }
                PermissionQueryResponse permissionQueryResponse = new PermissionQueryResponse();
                permissionQueryResponse.setUserId(permissionEntity.getUserId());
                permissionQueryResponse.setRoles(permissionEntity.getRoles());

                Set<String> permissionIds = Sets.newHashSet();
                List<PermissionDTO> permissionDTOS = Lists.newArrayList();

                for (UserRoleDTO roleDTO : permissionEntity.getRoles()) {
                    List<PermissionDTO> permissions = roleDTO.getPermissions();
                    for (PermissionDTO permissionDTO : permissions) {
                        if (!permissionIds.contains(permissionDTO.getId())) {
                            permissionIds.add(permissionDTO.getId());
                            permissionDTOS.add(permissionDTO);
                        }
                    }
                }
                permissionQueryResponse.setPermissions(permissionDTOS);
                return permissionQueryResponse;
            }

        });
    }

    @Override
    public ServiceResponse<String> addRole(RoleAddRequest roleAddRequest) {
        return super.queryProcess(BizScene.USER_ROLE_ADD, roleAddRequest, new QueryApiCallBack<String, String>() {

            @Override
            protected void checkParams(Object request) {
                RoleAddRequest addRequest = (RoleAddRequest) request;
                AssertUtil.notNull(addRequest.getRoleCode(), BizErrorCode.PARAM_ILLEGAL, "roleCode不为空");
                AssertUtil.notNull(addRequest.getRoleName(), BizErrorCode.PARAM_ILLEGAL, "roleName不为空");
            }

            @Override
            protected Object convert(Object request) {
                RoleAddRequest addRequest = (RoleAddRequest) request;
                return new String[]{addRequest.getRoleCode(), addRequest.getRoleName()};
            }

            @Override
            protected String doProcess(Object args) {
                String[] args1 = (String[]) args;
                return permissionManager.addRole(args1[0], args1[1]);
            }

            @Override
            protected String convertResult(String roleId) {
                return roleId;
            }

        });
    }


    @Override
    public ServiceResponse<String> addRoleRela(UserRoleRelaRequest relaRequest) {
        return super.queryProcess(BizScene.ROLE_RELA_ADD, relaRequest, new QueryApiCallBack<String, String>() {

            @Override
            protected void checkParams(Object request) {
                UserRoleRelaRequest roleRelaRequest = (UserRoleRelaRequest) request;
                AssertUtil.notNull(roleRelaRequest.getRoleId(), BizErrorCode.PARAM_ILLEGAL, "roleId 不为空");
                AssertUtil.notNull(roleRelaRequest.getUserId(), BizErrorCode.PARAM_ILLEGAL, "userId 不为空");
            }

            @Override
            protected String doProcess(Object args) {
                UserRoleRelaRequest roleRelaRequest = (UserRoleRelaRequest) args;
                return permissionManager.addUserRoleRela(roleRelaRequest.getRoleId(), roleRelaRequest.getUserId());
            }

            @Override
            protected String convertResult(String id) {
                return id;
            }

        });
    }

    @Autowired
    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }
}
