package com.odk.baseservice.impl.user;

import com.odk.base.context.TenantIdContext;
import com.odk.base.enums.user.TokenTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.util.I18nUtil;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserRegisterApi;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basemanager.api.user.IUserLoginManager;
import com.odk.basemanager.api.user.IUserRegisterManager;
import com.odk.basemanager.api.verificationcode.IVerificationCodeManager;
import com.odk.baseutil.annotation.BizProcess;
import com.odk.baseutil.convert.UserLoginConvert;
import com.odk.baseutil.convert.UserRegisterConvert;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.baseutil.enums.VerifyTypeEnum;
import com.odk.baseutil.request.UserRegisterNoAuthRequest;
import com.odk.baseutil.request.UserRegisterRequest;
import com.odk.baseutil.response.UserLoginResponse;
import com.odk.baseutil.userinfo.SessionContext;
import com.odk.baseutil.validate.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * UserRegisterService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Service
@AllArgsConstructor
public class UserRegisterService implements UserRegisterApi {

    private IVerificationCodeManager verificationCodeManager;

    private IUserRegisterManager userRegisterManager;

    private UserRegisterConvert userRegisterConvert;

    private UserAccessTokenRepository accessTokenRepository;

    private IUserLoginManager userLoginManager;

    private UserLoginConvert userLoginConvert;

    @Override
    @BizProcess(bizScene = BizScene.USER_REGISTER_LOGIN)
    public ServiceResponse<UserLoginResponse> loginAfterRegister(UserRegisterRequest userRegisterRequest) {
        ServiceResponse<String> response = userRegister(userRegisterRequest);
        if (response.isSuccess()) {
            ServiceResponse<UserLoginResponse> serviceResponse = ServiceResponse.valueOfSuccess(userLoginConvert.toResponse(userLoginManager.userLoginAfterRegister(response.getData())));
            serviceResponse.getData().setToken(SessionContext.getToken());
            return serviceResponse;
        }
        return ServiceResponse.valueOfError(BizErrorCode.SYSTEM_ERROR);
    }

    @Override
    @BizProcess(bizScene = BizScene.USER_REGISTER)
    public ServiceResponse<String> userRegister(UserRegisterRequest userRegisterRequest) {
        validateLoginValue(userRegisterRequest.getLoginType(), userRegisterRequest.getLoginId());

        //1.先判断 uniqueId 是否存在
        VerificationCodeDTO dto = userRegisterRequest.getVerificationCode();
        AssertUtil.notNull(dto, BizErrorCode.PARAM_ILLEGAL, I18nUtil.getMessage("verification.data.isnull"));
        //检查验证码是否有效:这里因为有 uniqueId 的原因，可以直接比较，如果没有 uniqueId，使用接口攻击会有风险
        dto.fillVerifyInfo(VerifySceneEnum.REGISTER, VerifyTypeEnum.MOBILE, userRegisterRequest.getLoginId());
        verificationCodeManager.verifyAndExecute(dto, (t) -> {
            UserAccessTokenDO byTokenTypeAndTokenValue = accessTokenRepository.findByTokenTypeAndTokenValueAndTenantId(userRegisterRequest.getLoginType(), userRegisterRequest.getLoginId(), TenantIdContext.getTenantId());
            AssertUtil.isNull(byTokenTypeAndTokenValue, BizErrorCode.USER_HAS_EXISTED, I18nUtil.getMessage("user.existed", userRegisterRequest.getLoginId()));
        });

        return ServiceResponse.valueOfSuccess(userRegisterManager.registerUser(userRegisterConvert.toDTO(userRegisterRequest)));

    }

    @Override
    public ServiceResponse<String> userRegisterNoAuth(UserRegisterNoAuthRequest noAuthRequest) {
        validateLoginValue(noAuthRequest.getLoginType(), noAuthRequest.getLoginId());
        return ServiceResponse.valueOfSuccess(userRegisterManager.registerUser(userRegisterConvert.toDTO(noAuthRequest)));
    }

    /**
     * 校验登录 id
     *
     * @param loginType
     * @param loginId
     */
    private void validateLoginValue(String loginType, String loginId) {
        if (TokenTypeEnum.MOBILE == TokenTypeEnum.getByCode(loginType)) {
            ValidationUtil.validateMobile(loginId);
        } else if (TokenTypeEnum.EMAIL == TokenTypeEnum.getByCode(loginType)) {
            ValidationUtil.validateEmail(loginId);
        }
    }
}
