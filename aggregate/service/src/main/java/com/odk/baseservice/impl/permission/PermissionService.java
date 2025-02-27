package com.odk.baseservice.impl.permission;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.PermissionApi;
import com.odk.baseutil.request.role.PermissionAddRequest;
import com.odk.basemanager.deal.permission.PermissionManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.enums.BizScene;
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
            protected void checkParams(BaseRequest request) {
                AssertUtil.notNull(permissionAddRequest, BizErrorCode.PARAM_ILLEGAL, "参数不为空");
                AssertUtil.notNull(permissionAddRequest.getRoleId(), BizErrorCode.PARAM_ILLEGAL, "roleId不为空");
                AssertUtil.notNull(permissionAddRequest.getPermissionCode(), BizErrorCode.PARAM_ILLEGAL, "permissionCode不为空");
                AssertUtil.notNull(permissionAddRequest.getPermissionName(), BizErrorCode.PARAM_ILLEGAL, "permissionName不为空");
            }

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
