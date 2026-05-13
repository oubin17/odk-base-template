package com.odk.baseservice.impl.verificationcode;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.verificationcode.VerificationCodeApi;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basemanager.impl.verificationcode.VerificationCodeManager;
import com.odk.baseutil.annotation.BizProcess;
import com.odk.baseutil.convert.VerificationCodeConvert;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.entity.VerificationCodeEntity;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.enums.UserQueryTypeEnum;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.baseutil.request.VerificationCodeRequest;
import com.odk.baseutil.validate.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * VerificationCodeService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
@Slf4j
@Service
@AllArgsConstructor
public class VerificationCodeService implements VerificationCodeApi {

    private VerificationCodeManager verificationCodeManager;

    private VerificationCodeConvert verificationCodeConvert;

    private UserQueryDomain userQueryDomain;


    /**
     * 无session的场景：注册、登录，verifyKey 是手机号
     * 有 session 场景：修改密码、重置密码，verifyKey 是用户id
     *
     * @param codeRequest
     * @return
     */
    @Override
    @BizProcess(bizScene = BizScene.VERIFICATION_CODE_GENERATE)
    public ServiceResponse<VerificationCodeEntity> generateCode(VerificationCodeRequest codeRequest) {
        VerificationCodeDTO dto = verificationCodeConvert.toDTO(codeRequest);
        if (VerifySceneEnum.MOBILE_SCENE_LIST.contains(dto.getVerifyScene())) {
            // 未登录场景：verifyKey 必须是手机号
            ValidationUtil.validateMobile(codeRequest.getVerifyKey());
            dto.setMobileOrEmail(codeRequest.getVerifyKey());
        } else {
            // 已登录场景：从 token 中获取用户id
            UserEntity userEntity = userQueryDomain.queryUser(UserQueryCriteria.builder().queryType(UserQueryTypeEnum.SESSION).build());
            AssertUtil.notNull(userEntity, BizErrorCode.USER_NOT_LOGIN);
            dto.setVerifyKey(userEntity.getUserId());
            dto.setMobileOrEmail(userEntity.getAccessToken().getTokenValue());
        }
        return ServiceResponse.valueOfSuccess(verificationCodeManager.generate(dto));
    }

    @Override
    @BizProcess(bizScene = BizScene.VERIFICATION_CODE_COMPARE)
    public ServiceResponse<Boolean> compare(VerificationCodeRequest codeRequest) {
        AssertUtil.notNull(codeRequest.getUniqueId(), BizErrorCode.VERIFY_CODE_UNIQUE_ERROR);
        VerificationCodeDTO dto = verificationCodeConvert.toDTO(codeRequest);
        return ServiceResponse.valueOfSuccess(verificationCodeManager.compareAndIncr(dto));
    }

}
