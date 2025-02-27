package com.odk.baseweb.permission;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.RoleApi;
import com.odk.baseutil.request.role.RoleAddRequest;
import com.odk.baseutil.request.role.UserRoleRelaRequest;
import com.odk.baseutil.response.PermissionQueryResponse;
import org.springframework.web.bind.annotation.*;

/**
 * RoleController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/2/27
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleApi roleApi;

    public RoleController(RoleApi roleApi) {
        this.roleApi = roleApi;
    }

    /**
     * 查询当前用户角色权限
     *
     * @return
     */
    @GetMapping("/info")
    public ServiceResponse<PermissionQueryResponse> currentUserRoles() {
        return this.roleApi.userRoles(null);
    }


    /**
     * 查询用户角色权限
     *
     * @param userId
     * @return
     */
    @SaCheckRole(value = {"ADMIN"})
    @GetMapping("/userId")
    public ServiceResponse<PermissionQueryResponse> queryUserPermission(@RequestParam("userId") String userId) {
        return this.roleApi.userRoles(userId);
    }


    /**
     * 添加角色
     *
     * @param roleAddRequest
     * @return
     */
    @SaCheckRole(value = {"ADMIN"})
    @PostMapping("/add")
    public ServiceResponse<String> addRole(@RequestBody RoleAddRequest roleAddRequest) {
        return this.roleApi.addRole(roleAddRequest);
    }

    /**
     * 添加用户角色
     *
     * @param relaRequest
     * @return
     */
    @SaCheckRole(value = {"ADMIN"})
    @GetMapping("/rela/add")
    public ServiceResponse<String> addRoleRela(UserRoleRelaRequest relaRequest) {
        return this.roleApi.addRoleRela(relaRequest);
    }


    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @SaCheckRole(value = {"ADMIN"})
    @DeleteMapping()
    public ServiceResponse<Boolean> deleteRole(@RequestParam("roleId") String roleId) {
        return this.roleApi.deleteRole(roleId);
    }

}
