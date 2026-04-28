package com.odk.baseservice.impl.user;

import com.odk.base.context.TenantIdContext;
import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.PasswordApi;
import com.odk.basedomain.model.user.UserIdentificationDO;
import com.odk.basedomain.repository.user.UserIdentificationRepository;
import com.odk.basemanager.api.user.IPasswordManager;
import com.odk.basemanager.api.verificationcode.IVerificationCodeManager;
import com.odk.baseutil.annotation.BizProcess;
import com.odk.baseutil.convert.PasswordConvert;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.baseutil.enums.VerifyTypeEnum;
import com.odk.baseutil.request.password.PasswordSetRequest;
import com.odk.baseutil.request.password.PasswordUpdateRequest;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
public class PasswordService implements PasswordApi {

    private IPasswordManager passwordManager;

    private PasswordConvert passwordConvert;

    private UserIdentificationRepository identificationRepository;

    private IVerificationCodeManager verificationCodeManager;

    @Override
    @BizProcess(bizScene = BizScene.PASSWORD_EXISTED)
    public ServiceResponse<Boolean> checkExisted(PasswordSetRequest passwordSetRequest) {
        AssertUtil.notNull(IdentificationTypeEnum.getByCode(passwordSetRequest.getIdentifyType()), BizErrorCode.PARAM_ILLEGAL);
        UserIdentificationDO userIdentificationDO = identificationRepository.findByUserIdAndIdentifyTypeAndTenantId(SessionContext.getLoginIdWithCheck(), passwordSetRequest.getIdentifyType(), TenantIdContext.getTenantId());
        return ServiceResponse.valueOfSuccess(userIdentificationDO != null);
    }

    @Override
    @BizProcess(bizScene = BizScene.PASSWORD_SET)
    public ServiceResponse<Boolean> setPassword(PasswordSetRequest passwordSetRequest) {
        UserIdentificationDO userIdentificationDO = identificationRepository.findByUserIdAndIdentifyTypeAndTenantId(SessionContext.getLoginIdWithCheck(), passwordSetRequest.getIdentifyType(), TenantIdContext.getTenantId());
        AssertUtil.isNull(userIdentificationDO, BizErrorCode.IDENTIFICATION_EXISTED);
        return ServiceResponse.valueOfSuccess(passwordManager.setPassword(passwordSetRequest));
    }

    @Override
    @BizProcess(bizScene = BizScene.PASSWORD_RESET)
    public ServiceResponse<Boolean> reSetPassword(PasswordSetRequest passwordSetRequest) {
        AssertUtil.notNull(passwordSetRequest.getVerificationCode(), BizErrorCode.PARAM_ILLEGAL);
        passwordSetRequest.getVerificationCode().fillVerifyInfo(VerifySceneEnum.PASSWORD_SET, VerifyTypeEnum.USER_ID, SessionContext.getLoginIdWithCheck());
        verificationCodeManager.compareAndIncr(passwordSetRequest.getVerificationCode());
        return ServiceResponse.valueOfSuccess(passwordManager.reSetPassword(passwordSetRequest));
    }

    @Override
    @BizProcess(bizScene = BizScene.PASSWORD_UPDATE)
    public ServiceResponse<Boolean> passwordUpdate(PasswordUpdateRequest passwordUpdateRequest) {
        return ServiceResponse.valueOfSuccess(passwordManager.updatePassword(passwordConvert.toDTO(passwordUpdateRequest)));
    }

}
