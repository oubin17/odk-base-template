package com.odk.baseservice.impl.user;

import com.odk.base.context.TenantIdContext;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.BaseResponse;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.PasswordApi;
import com.odk.basedomain.model.user.UserIdentificationDO;
import com.odk.basedomain.repository.user.UserIdentificationRepository;
import com.odk.basemanager.api.user.IPasswordManager;
import com.odk.basemanager.api.verificationcode.IVerificationCodeManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.convert.PasswordConvert;
import com.odk.baseutil.dto.user.PasswordUpdateDTO;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.baseutil.enums.VerifyTypeEnum;
import com.odk.baseutil.request.password.PasswordSetRequest;
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

    private IPasswordManager passwordManager;

    private PasswordConvert passwordConvert;

    private UserIdentificationRepository identificationRepository;

    private IVerificationCodeManager verificationCodeManager;

    @Override
    public ServiceResponse<Boolean> setPassword(PasswordSetRequest passwordSetRequest) {
        return super.bizProcess(BizScene.PASSWORD_SET, passwordSetRequest, new ApiCallBack<Boolean, Boolean>() {

            @Override
            protected void checkParams(Object request) {
                UserIdentificationDO userIdentificationDO = identificationRepository.findByUserIdAndIdentifyTypeAndTenantId(SessionContext.getLoginIdWithCheck(), passwordSetRequest.getIdentifyType(), TenantIdContext.getTenantId());
                AssertUtil.isNull(userIdentificationDO, BizErrorCode.IDENTIFICATION_EXISTED);
            }

            @Override
            protected Boolean doProcess(Object args) {
                return passwordManager.setPassword(passwordSetRequest);
            }

            @Override
            protected Boolean convertResult(Boolean apiResult) {
                return apiResult;
            }

        });
    }

    @Override
    public ServiceResponse<Boolean> reSetPassword(PasswordSetRequest passwordSetRequest) {
        return super.bizProcess(BizScene.PASSWORD_RESET, passwordSetRequest, new ApiCallBack<Boolean, Boolean>() {

            @Override
            protected void checkParams(Object request) {
                AssertUtil.notNull(passwordSetRequest.getVerificationCode(), BizErrorCode.PARAM_ILLEGAL);
                passwordSetRequest.getVerificationCode().fillVerifyInfo(VerifySceneEnum.PASSWORD_SET, VerifyTypeEnum.USER_ID, SessionContext.getLoginIdWithCheck());
                verificationCodeManager.compareAndIncr(passwordSetRequest.getVerificationCode());
            }

            @Override
            protected Boolean doProcess(Object args) {
                return passwordManager.reSetPassword(passwordSetRequest);
            }

            @Override
            protected Boolean convertResult(Boolean apiResult) {
                return apiResult;
            }

        });
    }

    @Override
    public ServiceResponse<Boolean> passwordUpdate(PasswordUpdateRequest passwordUpdateRequest) {

        return super.strictBizProcess(BizScene.PASSWORD_UPDATE, passwordUpdateRequest, new StrictApiCallBack<Boolean, Boolean>() {

            @Override
            protected Object convert(BaseRequest request) {
                PasswordUpdateRequest updateRequest = (PasswordUpdateRequest) request;
                return passwordConvert.toDTO(updateRequest);
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
    public void setPasswordManager(IPasswordManager passwordManager) {
        this.passwordManager = passwordManager;
    }

    @Autowired
    public void setPasswordMapper(PasswordConvert passwordConvert) {
        this.passwordConvert = passwordConvert;
    }

    @Autowired
    public void setIdentificationRepository(UserIdentificationRepository identificationRepository) {
        this.identificationRepository = identificationRepository;
    }

    @Autowired
    public void setVerificationCodeManager(IVerificationCodeManager verificationCodeManager) {
        this.verificationCodeManager = verificationCodeManager;
    }
}
