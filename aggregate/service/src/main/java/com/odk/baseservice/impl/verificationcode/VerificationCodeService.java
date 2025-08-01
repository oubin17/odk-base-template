package com.odk.baseservice.impl.verificationcode;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.request.BaseRequest;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.verificationcode.VerificationCodeApi;
import com.odk.basemanager.deal.verificationcode.VerificationCodeManager;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.VerificationCodeEntity;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.mapper.VerificationCodeMapper;
import com.odk.baseutil.request.VerificationCodeRequest;
import com.odk.redisspringbootstarter.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class VerificationCodeService extends AbstractApiImpl implements VerificationCodeApi {

    private VerificationCodeManager verificationCodeManager;

    private VerificationCodeMapper verificationCodeMapper;

    @Override
    public ServiceResponse<VerificationCodeEntity> generateCode(VerificationCodeRequest codeRequest) {
        return super.strictBizProcess(BizScene.VERIFICATION_CODE_GENERATE, codeRequest, new StrictApiCallBack<VerificationCodeEntity, VerificationCodeEntity>() {

            @Override
            protected Object convert(BaseRequest request) {
                return verificationCodeMapper.toDTO(codeRequest);
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
                return verificationCodeMapper.toDTO(codeRequest);
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

    @Autowired
    public void setVerificationCodeManager(VerificationCodeManager verificationCodeManager) {
        this.verificationCodeManager = verificationCodeManager;
    }

    @Autowired
    public void setVerificationCodeMapper(VerificationCodeMapper verificationCodeMapper) {
        this.verificationCodeMapper = verificationCodeMapper;
    }

}
