package com.odk.baseservice.impl.user;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserRegisterApi;
import com.odk.basemanager.api.user.IUserRegisterManager;
import com.odk.basemanager.impl.verificationcode.VerificationCodeManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.dto.user.UserRegisterDTO;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.baseutil.mapper.UserRegisterMapper;
import com.odk.baseutil.request.UserRegisterRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * UserRegisterService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Service
public class UserRegisterService extends AbstractApiImpl implements UserRegisterApi, InitializingBean {

    private VerificationCodeManager verificationCodeManager;

    private IUserRegisterManager userRegisterManager;

    private UserRegisterMapper userRegisterMapper;

    @Value("${register.whiteList}")
    private String whiteList;

    private List<String> whiteListCache;

    @Override
    public ServiceResponse<String> userRegister(UserRegisterRequest userRegisterRequest) {
        return super.strictBizProcess(BizScene.USER_REGISTER, userRegisterRequest, new StrictApiCallBack<String, String>() {

            @Override
            protected void beforeProcess(BaseRequest request) {
                if (whiteListCache != null && whiteListCache.contains(userRegisterRequest.getLoginId())) {
                    return;
                }

                VerificationCodeDTO dto = userRegisterRequest.getVerificationCode();
                AssertUtil.notNull(dto, BizErrorCode.PARAM_ILLEGAL, "验证码不能为空");
                //检查验证码是否有效:这里因为有 uniqueId 的原因，可以直接比较，如果没有 uniqueId，使用接口攻击会有风险
                dto.setVerifyKey(userRegisterRequest.getLoginId());
                dto.setVerifyType(userRegisterRequest.getLoginType());
                dto.setVerifyScene(VerifySceneEnum.REGISTER);
                verificationCodeManager.compareAndIncr(dto);
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

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isNotBlank(whiteList)) {
            whiteListCache = Arrays.asList(whiteList.split(","));
        }
    }

    @Autowired
    public void setVerificationCodeManager(VerificationCodeManager verificationCodeManager) {
        this.verificationCodeManager = verificationCodeManager;
    }

    @Autowired
    public void setUserRegisterMapper(UserRegisterMapper userRegisterMapper) {
        this.userRegisterMapper = userRegisterMapper;
    }

    @Autowired
    public void setUserRegisterManager(IUserRegisterManager userRegisterManager) {
        this.userRegisterManager = userRegisterManager;
    }
}
