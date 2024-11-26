package com.odk.baseweb.permission;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.PermissionApi;
import com.odk.baseapi.request.role.RoleAddRequest;
import com.odk.baseapi.request.role.UserRoleRelaRequest;
import com.odk.baseapi.response.PermissionQueryResponse;
import org.springframework.web.bind.annotation.*;

/**
 * PermissionController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@RestController
@RequestMapping("/user/permission")
public class PermissionController {

    private PermissionApi permissionApi;

    public PermissionController(PermissionApi permissionApi) {
        this.permissionApi = permissionApi;
    }

    /**
     * 查询用户角色权限
     *
     * @param userId
     * @return
     */
    @SaCheckLogin
    @GetMapping("/userId")
    public ServiceResponse<PermissionQueryResponse> queryUserPermission(@RequestParam("userId") String userId) {
        return permissionApi.userPermission(userId);
    }

    /**
     * 添加角色
     *
     * @param roleAddRequest
     * @return
     */
    @SaCheckLogin
    @PostMapping("/role/add")
    public ServiceResponse<String> addRole(@RequestBody RoleAddRequest roleAddRequest) {
        return permissionApi.addRole(roleAddRequest);
    }

    /**
     * 用户-角色绑定
     *
     * @param relaRequest
     * @return
     */
    @SaCheckLogin
    @PostMapping("/role/rela/add")
    public ServiceResponse<String> addRoleRel(@RequestBody UserRoleRelaRequest relaRequest) {
        return permissionApi.addRoleRela(relaRequest);
    }
}
