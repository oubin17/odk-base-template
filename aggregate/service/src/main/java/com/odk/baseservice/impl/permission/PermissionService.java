package com.odk.baseservice.impl.permission;

import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.PermissionApi;
import com.odk.basemanager.deal.permission.PermissionManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.request.role.PermissionAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ServiceResponse<Boolean> addPermission(PermissionAddRequest permissionAddRequest) {
        return super.strictBizProcess(BizScene.USER_PERMISSION_ADD, permissionAddRequest, new StrictApiCallBack<Boolean, Boolean>() {

            @Override
            protected Object convert(BaseRequest request) {
                return request;
            }

            @Override
            protected Boolean doProcess(Object args) {
                return permissionManager.addPermission(permissionAddRequest);
            }

            @Override
            protected Boolean convertResult(Boolean result) {
                return result;
            }

        });
    }

    @Autowired
    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }
}
