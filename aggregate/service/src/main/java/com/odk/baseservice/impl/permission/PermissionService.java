package com.odk.baseservice.impl.permission;

import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.permission.PermissionApi;
import com.odk.basemanager.api.permission.IPermissionManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.dto.permission.PermissionDTO;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.request.role.PermissionAddRequest;
import com.odk.baseutil.request.role.PermissionQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PermissionService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/9
 */
@Service
public class PermissionService extends AbstractApiImpl implements PermissionApi {

    private IPermissionManager permissionManager;

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

    @Override
    public ServiceResponse<List<PermissionDTO>> permissionList(PermissionQueryRequest permissionQueryRequest) {
        return super.strictBizProcess(BizScene.PERMISSION_LIST, permissionQueryRequest, new StrictApiCallBack<List<PermissionDTO>, List<PermissionDTO>>() {

            @Override
            protected List<PermissionDTO> doProcess(Object args) {
                return permissionManager.permissionList(permissionQueryRequest);
            }

            @Override
            protected List<PermissionDTO> convertResult(List<PermissionDTO> result) {
                return result;
            }

        });
    }

    @Autowired
    public void setPermissionManager(IPermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }
}
