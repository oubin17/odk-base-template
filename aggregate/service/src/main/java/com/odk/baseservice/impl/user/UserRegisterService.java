package com.odk.baseservice.impl.user;

import com.odk.base.context.TenantIdContext;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.util.I18nUtil;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.BaseResponse;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserRegisterApi;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basemanager.api.user.IUserLoginManager;
import com.odk.basemanager.api.user.IUserRegisterManager;
import com.odk.basemanager.api.verificationcode.IVerificationCodeManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.convert.UserLoginConvert;
import com.odk.baseutil.convert.UserRegisterConvert;
import com.odk.baseutil.dto.user.UserRegisterDTO;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.baseutil.enums.VerifyTypeEnum;
import com.odk.baseutil.request.UserRegisterRequest;
import com.odk.baseutil.response.UserLoginResponse;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserRegisterService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Service
@AllArgsConstructor
public class UserRegisterService extends AbstractApiImpl implements UserRegisterApi, InitializingBean {

    private IVerificationCodeManager verificationCodeManager;

    private IUserRegisterManager userRegisterManager;

    private UserRegisterConvert userRegisterConvert;

    private UserAccessTokenRepository accessTokenRepository;

    private IUserLoginManager userLoginManager;

    private UserLoginConvert userLoginConvert;

//    @Value("${register.whiteList}")
//    private String whiteList;

    private List<String> whiteListCache;


    @Override
    public ServiceResponse<UserLoginResponse> loginAfterRegister(UserRegisterRequest userRegisterRequest) {
        return super.strictBizProcess(BizScene.USER_REGISTER, userRegisterRequest, new StrictApiCallBack<UserEntity, UserLoginResponse>() {

            @Override
            protected UserEntity doProcess(Object args) {
                ServiceResponse<String> response = userRegister(userRegisterRequest);
                if (response.isSuccess()) {
                    return userLoginManager.userLoginAfterRegister(response.getData());
                }
                throw new BizException(BizErrorCode.SYSTEM_ERROR);
            }

            @Override
            protected UserLoginResponse convertResult(UserEntity apiResult) {
                return userLoginConvert.toResponse(apiResult);
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void afterProcess(BaseResponse response) {
                if (response.isSuccess()) {
                    ServiceResponse<UserLoginResponse> userLoginResponseServiceResponse = (ServiceResponse<UserLoginResponse>) response;
                    userLoginResponseServiceResponse.getData().setToken(SessionContext.getToken());
                }
            }
        });
    }

    @Override
    public ServiceResponse<String> userRegister(UserRegisterRequest userRegisterRequest) {
        return super.strictBizProcess(BizScene.USER_REGISTER, userRegisterRequest, new StrictApiCallBack<String, String>() {

            @Override
            protected void checkParams(BaseRequest request) {

                //1.先判断 uniqueId 是否存在
                VerificationCodeDTO dto = userRegisterRequest.getVerificationCode();
                AssertUtil.notNull(dto, BizErrorCode.PARAM_ILLEGAL, I18nUtil.getMessage("verification.data.isnull"));
                //检查验证码是否有效:这里因为有 uniqueId 的原因，可以直接比较，如果没有 uniqueId，使用接口攻击会有风险
                dto.fillVerifyInfo(VerifySceneEnum.REGISTER, VerifyTypeEnum.MOBILE, userRegisterRequest.getLoginId());


                verificationCodeManager.verifyAndExecute(dto, (t) -> {
                    UserAccessTokenDO byTokenTypeAndTokenValue = accessTokenRepository.findByTokenTypeAndTokenValueAndTenantId(userRegisterRequest.getLoginType(), userRegisterRequest.getLoginId(), TenantIdContext.getTenantId());
                    AssertUtil.isNull(byTokenTypeAndTokenValue, BizErrorCode.USER_HAS_EXISTED, I18nUtil.getMessage("user.existed", userRegisterRequest.getLoginId()));
                });

//                boolean compare = verificationCodeManager.compare(dto);
//                AssertUtil.isTrue(compare, BizErrorCode.VERIFY_CODE_UNMATCHED);
//
//                //2. 检查登录 ID
//                UserAccessTokenDO byTokenTypeAndTokenValue = accessTokenRepository.findByTokenTypeAndTokenValueAndTenantId(userRegisterRequest.getLoginType(), userRegisterRequest.getLoginId(), TenantIdContext.getTenantId());
//                AssertUtil.isNull(byTokenTypeAndTokenValue, BizErrorCode.USER_HAS_EXISTED, I18nUtil.getMessage("user.existed", userRegisterRequest.getLoginId()));
//                //3. 校验验证吗
//                verificationCodeManager.compareAndIncr(dto);

            }

            @Override
            protected Object convert(BaseRequest request) {
                UserRegisterRequest registerRequest = (UserRegisterRequest) request;
                return userRegisterConvert.toDTO(registerRequest);
            }

            @Override
            protected String doProcess(Object args) {
                UserRegisterDTO userRegisterDTO = (UserRegisterDTO) args;
                return userRegisterManager.registerUser(userRegisterDTO);
            }

            @Override
            protected String convertResult(String apiResult) {
                return apiResult;
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        if (StringUtils.isNotBlank(whiteList)) {
//            whiteListCache = Arrays.asList(whiteList.split(","));
//        }
    }

}
