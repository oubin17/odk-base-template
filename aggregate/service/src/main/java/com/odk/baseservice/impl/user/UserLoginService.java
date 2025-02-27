package com.odk.baseservice.impl.user;

import cn.dev33.satoken.stp.StpUtil;
import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.enums.user.TokenTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.BaseResponse;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserLoginApi;
import com.odk.baseutil.request.UserLoginRequest;
import com.odk.baseutil.response.UserLoginResponse;
import com.odk.basemanager.deal.user.UserLoginManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.dto.user.UserLoginDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class UserLoginService extends AbstractApiImpl implements UserLoginApi {

    private UserLoginManager userLoginManager;

    @Override
    public ServiceResponse<UserLoginResponse> userLogin(UserLoginRequest userLoginRequest) {
        return super.strictBizProcess(BizScene.USER_LOGIN, userLoginRequest, new StrictApiCallBack<UserLoginResponse, UserLoginResponse>() {

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
                UserEntity userEntity = userLoginManager.userLogin(userLoginDTO);
                UserLoginResponse userLoginResponse = new UserLoginResponse();
                BeanUtils.copyProperties(userEntity, userLoginResponse);
                return userLoginResponse;
            }

            @Override
            protected UserLoginResponse convertResult(UserLoginResponse apiResult) {
                return apiResult;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void afterProcess(BaseResponse response) {
                if (response.isSuccess()) {
                    ServiceResponse<UserLoginResponse> userLoginResponseServiceResponse = (ServiceResponse<UserLoginResponse>) response;
                    userLoginResponseServiceResponse.getData().setToken(StpUtil.getTokenInfo().getTokenValue());
                }
            }
        });
    }

    @Override
    public ServiceResponse<Boolean> userLogout() {
        return super.bizProcess(BizScene.USER_LOGOUT, null, new ApiCallBack<Boolean, Boolean>() {

            @Override
            protected Boolean doProcess(Object args) {
                if (!SessionContext.isLogin()) {
                    log.error("当前用户非登录态，登录注销失败！");
                    throw new BizException(BizErrorCode.PARAM_ILLEGAL, "用户非登录态，登出异常");
                }
                return userLoginManager.userLogout();

            }

            @Override
            protected Boolean convertResult(Boolean apiResult) {
                return apiResult;
            }
        });
    }

    @Autowired
    public void setUserLoginManager(UserLoginManager userLoginManager) {
        this.userLoginManager = userLoginManager;
    }
}
