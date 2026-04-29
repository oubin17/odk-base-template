package com.odk.baseservice.impl.user;

import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.util.I18nUtil;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserLoginApi;
import com.odk.basemanager.api.user.IUserLoginManager;
import com.odk.baseutil.annotation.BizProcess;
import com.odk.baseutil.convert.UserLoginConvert;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.baseutil.enums.VerifyTypeEnum;
import com.odk.baseutil.request.UserLoginRequest;
import com.odk.baseutil.response.UserLoginResponse;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
public class UserLoginService implements UserLoginApi {

    private IUserLoginManager userLoginManager;

    private UserLoginConvert userLoginConvert;

    @Override
    @BizProcess(bizScene = BizScene.USER_LOGIN)
    public ServiceResponse<UserLoginResponse> userLogin(UserLoginRequest userLoginRequest) {

        String identifyType = userLoginRequest.getIdentifyType();
        if (IdentificationTypeEnum.PASSWORD.getCode().equals(identifyType)) {
            AssertUtil.notNull(userLoginRequest.getIdentifyValue(), BizErrorCode.PARAM_ILLEGAL);
        } else if (IdentificationTypeEnum.VERIFICATION_CODE.getCode().equals(identifyType)) {
            AssertUtil.notNull(userLoginRequest.getVerificationCode(), BizErrorCode.PARAM_ILLEGAL);
            //默认手机号登录，暂不支持邮箱
            userLoginRequest.getVerificationCode().fillVerifyInfo(VerifySceneEnum.LOGIN, VerifyTypeEnum.MOBILE, userLoginRequest.getLoginId());
        }
        return ServiceResponse.valueOfSuccess(userLoginManager.userLogin(userLoginConvert.toDTO(userLoginRequest)));

    }

    @Override
    @BizProcess(bizScene = BizScene.USER_LOGOUT)
    public ServiceResponse<Boolean> userLogout() {

        if (!SessionContext.isLogin()) {
            throw new BizException(BizErrorCode.PARAM_ILLEGAL, I18nUtil.getMessage("user.not.login"));
        }
        return ServiceResponse.valueOfSuccess(userLoginManager.userLogout());

    }
}
