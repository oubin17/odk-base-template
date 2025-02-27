package com.odk.baseweb.permission;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.RoleApi;
import com.odk.baseutil.request.role.RoleAddRequest;
import com.odk.baseutil.request.role.UserRoleRelaRequest;
import com.odk.baseutil.enums.InnerRoleEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RoleManagerController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/2/27
 */
@RestController
@RequestMapping("/role/manager")
public class RoleManagerController {

    private final RoleApi roleApi;

    public RoleManagerController(RoleApi roleApi) {
        this.roleApi = roleApi;
    }
    /**
     * 添加角色
     *
     * @param roleAddRequest
     * {@link InnerRoleEnum}
     * @return
     */
    @SaCheckRole(value = {"ADMIN"})
    @PostMapping("/role/add")
    public ServiceResponse<String> addRole(@RequestBody RoleAddRequest roleAddRequest) {
        return this.roleApi.addRole(roleAddRequest);
    }

    /**
     * 用户-角色绑定
     *
     * @param relaRequest
     * @return
     */
    @SaCheckRole(value = {"ADMIN"})
    @PostMapping("/role/rela/add")
    public ServiceResponse<String> addRoleRel(@RequestBody UserRoleRelaRequest relaRequest) {
        return this.roleApi.addRoleRela(relaRequest);
    }

}
