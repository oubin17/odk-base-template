package com.odk.baseweb.permission;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.RoleApi;
import com.odk.baseutil.dto.permission.UserRoleDTO;
import com.odk.baseutil.request.role.RoleAddRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
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

    /**
     * 查询当前用户角色权限
     *
     * @return
     */
    @SaCheckRole(value = {"ADMIN"})
    @GetMapping("/list")
    public ServiceResponse<List<UserRoleDTO>> allRoleList() {
        return this.roleApi.roleList();
    }

}
