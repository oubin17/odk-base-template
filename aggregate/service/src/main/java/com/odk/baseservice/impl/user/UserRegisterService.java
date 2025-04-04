package com.odk.baseservice.impl.user;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserRegisterApi;
import com.odk.basemanager.deal.user.UserRegisterManager;
import com.odk.basemanager.deal.verificationcode.SmsVerificationManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.dto.user.UserRegisterDTO;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.mapper.UserRegisterMapper;
import com.odk.baseutil.request.UserRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserRegisterService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Service
public class UserRegisterService extends AbstractApiImpl implements UserRegisterApi {

    private SmsVerificationManager smsVerificationManager;

    private UserRegisterManager userRegisterManager;

    private UserRegisterMapper userRegisterMapper;

    @Override
    public ServiceResponse<String> userRegister(UserRegisterRequest userRegisterRequest) {
        return super.strictBizProcess(BizScene.USER_REGISTER, userRegisterRequest, new StrictApiCallBack<String, String>() {

            @Override
            protected void beforeProcess(BaseRequest request) {
                //检查验证码是否有效
                UserRegisterRequest registerRequest = (UserRegisterRequest) request;
                AssertUtil.isTrue(smsVerificationManager.verifySms(registerRequest.getVerificationCode()), BizErrorCode.VERIFY_CODE_UNMATCHED, "验证码不匹配，请重新输入");
            }

            @Override
            protected Object convert(BaseRequest request) {
                UserRegisterRequest registerRequest = (UserRegisterRequest) request;
                return userRegisterMapper.toDTO(registerRequest);
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

    @Autowired
    public void setUserRegisterManager(UserRegisterManager userRegisterManager) {
        this.userRegisterManager = userRegisterManager;
    }

    @Autowired
    public void setSmsVerificationManager(SmsVerificationManager smsVerificationManager) {
        this.smsVerificationManager = smsVerificationManager;
    }

    @Autowired
    public void setUserRegisterMapper(UserRegisterMapper userRegisterMapper) {
        this.userRegisterMapper = userRegisterMapper;
    }
}
