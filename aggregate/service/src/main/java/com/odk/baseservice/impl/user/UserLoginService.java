package com.odk.baseservice.impl.user;

import cn.dev33.satoken.stp.StpUtil;
import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.BaseResponse;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserLoginApi;
import com.odk.basemanager.deal.user.UserLoginManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.dto.user.UserLoginDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.baseutil.mapper.UserLoginMapper;
import com.odk.baseutil.request.UserLoginRequest;
import com.odk.baseutil.response.UserLoginResponse;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.extern.slf4j.Slf4j;
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

    private UserLoginMapper userLoginMapper;

    @Override
    public ServiceResponse<UserLoginResponse> userLogin(UserLoginRequest userLoginRequest) {

//        return ServiceTemplate.execute(
//                BizScene.USER_LOGIN,
//                userLoginRequest,
//                loginRequest -> {
//                    String identifyType = userLoginRequest.getIdentifyType();
//                    if (IdentificationTypeEnum.PASSWORD.getCode().equals(identifyType)) {
//                        AssertUtil.notNull(userLoginRequest.getIdentifyValue(), BizErrorCode.PARAM_ILLEGAL, "密码不为空");
//                    } else if (IdentificationTypeEnum.VERIFICATION_CODE.getCode().equals(identifyType)) {
//                        AssertUtil.notNull(userLoginRequest.getVerificationCode(), BizErrorCode.PARAM_ILLEGAL, "验证码不为空");
//                    }
//                },
//                loginRequest -> {
//                    UserLoginDTO userLoginDTO = userLoginMapper.toDTO(userLoginRequest);
//                    UserEntity userEntity = userLoginManager.userLogin(userLoginDTO);
//                    return userLoginMapper.toResponse(userEntity);
//                },
//                userLoginResponse -> {
//                    if (userLoginResponse.isSuccess()) {
//                        userLoginResponse.getData().setToken(StpUtil.getTokenInfo().getTokenValue());
//                    }
//                });


        return super.strictBizProcess(BizScene.USER_LOGIN, userLoginRequest, new StrictApiCallBack<UserLoginResponse, UserLoginResponse>() {

            @Override
            protected void checkParams(BaseRequest request) {
                String identifyType = userLoginRequest.getIdentifyType();
                if (IdentificationTypeEnum.PASSWORD.getCode().equals(identifyType)) {
                    AssertUtil.notNull(userLoginRequest.getIdentifyValue(), BizErrorCode.PARAM_ILLEGAL, "密码不为空");
                } else if (IdentificationTypeEnum.VERIFICATION_CODE.getCode().equals(identifyType)) {
                    AssertUtil.notNull(userLoginRequest.getVerificationCode(), BizErrorCode.PARAM_ILLEGAL, "验证码不为空");
                }
            }

            @Override
            protected void beforeProcess(BaseRequest request) {
                if (IdentificationTypeEnum.VERIFICATION_CODE.getCode().equals(userLoginRequest.getIdentifyType())) {
                    userLoginRequest.getVerificationCode().setVerifyScene(VerifySceneEnum.LOGIN);
                    userLoginRequest.getVerificationCode().setVerifyKey(userLoginRequest.getLoginId());
                    userLoginRequest.getVerificationCode().setVerifyType(userLoginRequest.getLoginType());
                }

            }

            @Override
            protected Object convert(BaseRequest request) {
                UserLoginRequest loginRequest = (UserLoginRequest) request;
                return userLoginMapper.toDTO(loginRequest);
            }

            @Override
            protected UserLoginResponse doProcess(Object args) {
                UserLoginDTO userLoginDTO = (UserLoginDTO) args;
                UserEntity userEntity = userLoginManager.userLogin(userLoginDTO);
                return userLoginMapper.toResponse(userEntity);
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

    @Autowired
    public void setUserLoginMapper(UserLoginMapper userLoginMapper) {
        this.userLoginMapper = userLoginMapper;
    }
}
