package com.odk.baseservice.impl.user;

import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.enums.user.TokenTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserLoginApi;
import com.odk.baseapi.request.UserLoginRequest;
import com.odk.baseapi.response.UserLoginResponse;
import com.odk.basemanager.entity.UserEntity;
import com.odk.basemanager.entity.UserLoginEntity;
import com.odk.basemanager.deal.user.UserLoginManager;
import com.odk.basemanager.deal.user.UserQueryManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.basemanager.dto.UserLoginDTO;
import com.odk.baseutil.enums.BizScene;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserLoginService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Service
public class UserLoginService extends AbstractApiImpl implements UserLoginApi {

    private UserLoginManager userLoginManager;

    private UserQueryManager queryManager;

    @Override
    public ServiceResponse<UserLoginResponse> userLogin(UserLoginRequest userLoginRequest) {
        return super.bizProcess(BizScene.USER_LOGIN, userLoginRequest, new AbstractApiImpl.ApiCallBack<UserLoginResponse, UserLoginResponse>() {

            @Override
            protected void checkParams(BaseRequest request) {
                super.checkParams(request);
                UserLoginRequest loginRequest = (UserLoginRequest) request;
                AssertUtil.notNull(loginRequest.getLoginId(), BizErrorCode.PARAM_ILLEGAL, "loginId is null.");
                AssertUtil.notNull(TokenTypeEnum.getByCode(loginRequest.getLoginType()), BizErrorCode.PARAM_ILLEGAL, "loginType is null.");
                AssertUtil.notNull(IdentificationTypeEnum.getByCode(loginRequest.getIdentifyType()), BizErrorCode.PARAM_ILLEGAL, "identifyType is null.");
                AssertUtil.notNull(loginRequest.getIdentifyValue(), BizErrorCode.PARAM_ILLEGAL, "identifyValue is null.");
            }

            @Override
            protected Object convert(BaseRequest request) {
                UserLoginRequest loginRequest = (UserLoginRequest) request;
                UserLoginDTO userLoginDTO = new UserLoginDTO();
                BeanUtils.copyProperties(loginRequest, userLoginDTO);
                return userLoginDTO;
            }

            @Override
            protected UserLoginResponse doProcess(Object args) {
                UserLoginDTO userLoginDTO = (UserLoginDTO) args;
                UserLoginEntity userLoginEntity = userLoginManager.userLogin(userLoginDTO);
                UserEntity userEntity = queryManager.queryByUserId(userLoginEntity.getUserId());
                UserLoginResponse userLoginResponse = new UserLoginResponse();
                userLoginResponse.setToken(userLoginEntity.getToken());
                BeanUtils.copyProperties(userEntity, userLoginResponse);
                return userLoginResponse;

            }

            @Override
            protected UserLoginResponse convertResult(UserLoginResponse apiResult) {
                return apiResult;
            }
        });
    }

    @Autowired
    public void setQueryManager(UserQueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @Autowired
    public void setUserLoginManager(UserLoginManager userLoginManager) {
        this.userLoginManager = userLoginManager;
    }
}
