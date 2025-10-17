package com.odk.baseweb.permission;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.RoleApi;
import com.odk.baseutil.enums.InnerRoleEnum;
import com.odk.baseutil.request.role.UserRoleRelaRequest;
import com.odk.baseutil.response.PermissionQueryResponse;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-角色关系管理
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/23
 */
@RestController
@RequestMapping("/role/rela")
public class RoleRelaController {

    private final RoleApi roleApi;

    public RoleRelaController(RoleApi roleApi) {
        this.roleApi = roleApi;
    }

    /**
     * 给用户添加角色
     *
     * @param relaRequest
     * @return
     */
    @SaCheckRole(value = {InnerRoleEnum.ADMIN_ROLE})
    @PostMapping("/add")
    public ServiceResponse<String> addRoleRela(UserRoleRelaRequest relaRequest) {
        return this.roleApi.addRoleRela(relaRequest);
    }

    /**
     * 去除用户角色
     *
     * @param relaRequest
     * @return
     */
    @SaCheckRole(value = {InnerRoleEnum.ADMIN_ROLE})
    @PostMapping("/delete")
    public ServiceResponse<Boolean> deleteRoleRela(UserRoleRelaRequest relaRequest) {
        return this.roleApi.deleteRoleRela(relaRequest);
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
    @SaCheckRole(value = {InnerRoleEnum.ADMIN_ROLE})
    @GetMapping("/userId")
    public ServiceResponse<PermissionQueryResponse> queryUserPermission(@RequestParam("userId") String userId) {
        return this.roleApi.userRoles(userId);
    }

}
