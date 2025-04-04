package com.odk.baseservice.impl.user;

import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.BaseResponse;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.PasswordApi;
import com.odk.basemanager.deal.user.PasswordManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.dto.user.PasswordUpdateDTO;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.mapper.PasswordMapper;
import com.odk.baseutil.request.password.PasswordUpdateRequest;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PasswordService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/3/7
 */
@Slf4j
@Service
public class PasswordService extends AbstractApiImpl implements PasswordApi {

    private PasswordManager passwordManager;

    private PasswordMapper passwordMapper;

    @Override
    public ServiceResponse<Boolean> passwordUpdate(PasswordUpdateRequest passwordUpdateRequest) {

        return super.strictBizProcess(BizScene.PASSWORD_UPDATE, passwordUpdateRequest, new StrictApiCallBack<Boolean, Boolean>() {

            @Override
            protected Object convert(BaseRequest request) {
                PasswordUpdateRequest updateRequest = (PasswordUpdateRequest) request;
                return passwordMapper.toDTO(updateRequest);
            }

            @Override
            protected Boolean doProcess(Object args) {
                PasswordUpdateDTO updateDTO = (PasswordUpdateDTO) args;
                return passwordManager.updatePassword(updateDTO);
            }

            @Override
            protected Boolean convertResult(Boolean apiResult) {
                return apiResult;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void afterProcess(BaseResponse response) {
                ServiceResponse<Boolean> serviceResponse = (ServiceResponse<Boolean>) response;
                if (serviceResponse.getData()!= null && serviceResponse.getData()) {
                    SessionContext.logOut();
                }
            }
        });
    }

    @Autowired
    public void setPasswordManager(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
    }

    @Autowired
    public void setPasswordMapper(PasswordMapper passwordMapper) {
        this.passwordMapper = passwordMapper;
    }
}
