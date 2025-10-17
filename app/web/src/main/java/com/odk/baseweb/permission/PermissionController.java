package com.odk.baseweb.permission;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.PermissionApi;
import com.odk.baseutil.dto.permission.PermissionDTO;
import com.odk.baseutil.enums.InnerRoleEnum;
import com.odk.baseutil.request.role.PermissionAddRequest;
import com.odk.baseutil.request.role.PermissionQueryRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PermissionController
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionApi permissionApi;

    public PermissionController(PermissionApi permissionApi) {
        this.permissionApi = permissionApi;
    }

    /**
     * 添加权限
     *
     * @param permissionAddRequest
     * @return
     */
    @SaCheckRole(value = {InnerRoleEnum.ADMIN_ROLE})
    @PostMapping("/add")
    public ServiceResponse<Boolean> addPermission(@RequestBody PermissionAddRequest permissionAddRequest) {
        return this.permissionApi.addPermission(permissionAddRequest);
    }

    /**
     * 权限列表
     *
     * @param permissionQueryRequest
     * @return
     */
    @SaCheckRole(value = {InnerRoleEnum.ADMIN_ROLE})
    @GetMapping("/list")
    public ServiceResponse<List<PermissionDTO>> permissionList(@RequestBody PermissionQueryRequest permissionQueryRequest) {
        return this.permissionApi.permissionList(permissionQueryRequest);
    }

}
