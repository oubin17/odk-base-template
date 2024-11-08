package com.odk.baseweb.permission;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.PermissionApi;
import com.odk.baseapi.response.PermissionQueryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @SaCheckLogin
    @GetMapping("/userId")
    public ServiceResponse<PermissionQueryResponse> queryUserPermission(@RequestParam("userId") String userId) {
        return permissionApi.userPermission(userId);
    }
}
