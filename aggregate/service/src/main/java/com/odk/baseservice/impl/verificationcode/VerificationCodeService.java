package com.odk.baseservice.impl.verificationcode;

import com.google.common.collect.ImmutableSet;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.verificationcode.VerificationCodeApi;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basemanager.impl.verificationcode.VerificationCodeManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.convert.VerificationCodeConvert;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.entity.VerificationCodeEntity;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.enums.UserQueryTypeEnum;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.baseutil.request.VerificationCodeRequest;
import com.odk.baseutil.userinfo.SessionContext;
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
public class VerificationCodeService extends AbstractApiImpl implements VerificationCodeApi {

    private VerificationCodeManager verificationCodeManager;

    private VerificationCodeConvert verificationCodeConvert;

    private UserQueryDomain userQueryDomain;

    /**
     * 不从 token 中获取手机号的场景：注册、登录。
     */
    private static final ImmutableSet<VerifySceneEnum> SCENE_LIST = ImmutableSet.of(VerifySceneEnum.REGISTER, VerifySceneEnum.LOGIN);

    @Override
    public ServiceResponse<VerificationCodeEntity> generateCode(VerificationCodeRequest codeRequest) {
        return super.strictBizProcess(BizScene.VERIFICATION_CODE_GENERATE, codeRequest, new StrictApiCallBack<VerificationCodeEntity, VerificationCodeEntity>() {

            @Override
            protected Object convert(BaseRequest request) {
                VerificationCodeDTO dto = verificationCodeConvert.toDTO(codeRequest);
                if (SCENE_LIST.contains(dto.getVerifyScene())) {
                    AssertUtil.notNull(codeRequest.getVerifyKey(), BizErrorCode.PARAM_ILLEGAL);
                } else {
                    //从 token 中获取用户手机号
                    dto.setVerifyKey(SessionContext.getLoginIdWithCheck());
                }
                return dto;
            }

            @Override
            protected VerificationCodeEntity doProcess(Object args) {
                VerificationCodeDTO verificationCodeDTO = (VerificationCodeDTO) args;
                return verificationCodeManager.generate(verificationCodeDTO);
            }

            @Override
            protected VerificationCodeEntity convertResult(VerificationCodeEntity apiResult) {
                return apiResult;
            }
        });
    }

    @Override
    public ServiceResponse<Boolean> compare(VerificationCodeRequest codeRequest) {
        return super.strictBizProcess(BizScene.VERIFICATION_CODE_COMPARE, codeRequest, new StrictApiCallBack<Boolean, Boolean>() {

            @Override
            protected void checkParams(BaseRequest request) {
                AssertUtil.notNull(codeRequest.getUniqueId(), BizErrorCode.VERIFY_CODE_UNIQUE_ERROR);
            }

            @Override
            protected Object convert(BaseRequest request) {
                return verificationCodeConvert.toDTO(codeRequest);
            }

            @Override
            protected Boolean doProcess(Object args) {
                VerificationCodeDTO verificationCodeDTO = (VerificationCodeDTO) args;
                return verificationCodeManager.compareAndIncr(verificationCodeDTO);
            }

            @Override
            protected Boolean convertResult(Boolean apiResult) {
                return apiResult;
            }

        });
    }

}
