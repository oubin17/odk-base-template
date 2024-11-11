package com.odk.baseservice.impl.user;

import cn.dev33.satoken.stp.StpUtil;
import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.enums.user.TokenTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.BaseResponse;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserLoginApi;
import com.odk.baseapi.request.UserLoginRequest;
import com.odk.baseapi.request.UserLogoutRequest;
import com.odk.baseapi.response.UserLoginResponse;
import com.odk.basemanager.deal.user.UserLoginManager;
import com.odk.basemanager.deal.user.UserQueryManager;
import com.odk.basemanager.dto.UserLoginDTO;
import com.odk.basemanager.entity.UserEntity;
import com.odk.basemanager.entity.UserLoginEntity;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.enums.BizScene;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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

    private UserQueryManager queryManager;

    @Override
    public ServiceResponse<UserLoginResponse> userLogin(UserLoginRequest userLoginRequest) {
        return super.bizProcess(BizScene.USER_LOGIN, userLoginRequest, new ApiCallBack<UserLoginResponse, UserLoginResponse>() {

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
                    //设置登录session
                    StpUtil.login(userLoginResponseServiceResponse.getData().getUserId());
                    userLoginResponseServiceResponse.getData().setToken(StpUtil.getTokenInfo().getTokenValue());
                }
            }
        });
    }

    @Override
    public ServiceResponse<Boolean> userLogout(UserLogoutRequest logoutRequest) {
        return super.bizProcess(BizScene.USER_LOGOUT, logoutRequest, new AbstractApiImpl.ApiCallBack<Boolean, Boolean>() {
            @Override
            protected void checkParams(BaseRequest request) {
                super.checkParams(request);
                UserLogoutRequest logout = (UserLogoutRequest) request;
                AssertUtil.notNull(logout.getUserId(), BizErrorCode.PARAM_ILLEGAL, "用户ID不存在");
            }

            @Override
            protected void beforeProcess(BaseRequest request) {
                UserLogoutRequest logout = (UserLogoutRequest) request;
                if (!StpUtil.isLogin()) {
                    logout.setLogin(false);
                    log.error("当前用户非登录态，登录注销失败！用户ID={}", logout.getUserId());
                }
            }

            @Override
            protected Object convert(BaseRequest request) {
                UserLogoutRequest logout = (UserLogoutRequest) request;
                return Arrays.asList(logout.getUserId(), logout.isLogin());
            }

            @Override
            protected Boolean doProcess(Object args) {
                List request = (List) args;
                Boolean isLogin = (Boolean) request.get(1);
                if (isLogin) {
                    return userLoginManager.userLogout((String) request.get(0));
                } else {
                    return false;
                }
            }

            @Override
            protected Boolean convertResult(Boolean apiResult) {
                return apiResult;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void afterProcess(BaseResponse response) {
                if (response.isSuccess()) {
                    ServiceResponse<Boolean> logoutResponse = (ServiceResponse<Boolean>) response;
                    if (logoutResponse.getData()) {
                        StpUtil.logout();
                        log.info("删除登录token成功，用户ID={}", logoutRequest.getUserId());
                    }
                }
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
