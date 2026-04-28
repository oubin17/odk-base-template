package com.odk.basemanager.impl.verificationcode;

import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.basedomain.domain.VerificationCodeDomain;
import com.odk.baseinfra.verificationcode.LocalVerificationGenerate;
import com.odk.basemanager.api.verificationcode.IVerificationCodeManager;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.VerificationCodeEntity;
import com.odk.baseutil.enums.VerificationCodeStatusEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * SmsVerificationManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/29
 */
@Slf4j
@Service
@AllArgsConstructor
public class VerificationCodeManager implements IVerificationCodeManager {

    private LocalVerificationGenerate verificationGenerate;

    private VerificationCodeDomain verificationCodeDomain;

    @Override
    public VerificationCodeEntity generate(VerificationCodeDTO verificationCodeDTO) {
        try {
            VerificationCodeEntity generate = verificationGenerate.generate(verificationCodeDTO);
            if (generate != null) {
                verificationCodeDomain.saveVerificationCodeFlow(verificationCodeDTO, generate.getUniqueId(), VerificationCodeStatusEnum.GENERATE_SUCCESS, null);
            }
            return generate;
        } catch (Exception e) {
            log.error("生成验证码发生未知异常，异常信息:", e);
            verificationCodeDomain.saveVerificationCodeFlow(verificationCodeDTO, null, VerificationCodeStatusEnum.GENERATE_FAIL, e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean compare(VerificationCodeDTO verificationCodeDTO) {
        return verificationGenerate.compareUniqueId(verificationCodeDTO);
    }

    /**
     * 对比并校验
     *
     * @param verificationCodeDTO
     * @return
     */
    @Override
    public boolean compareAndIncr(VerificationCodeDTO verificationCodeDTO) {
        try {
            boolean result = verificationGenerate.compareAndIncr(verificationCodeDTO);
            if (result) {
                verificationCodeDomain.verifySuccess(verificationCodeDTO.getUniqueId());
            }
            return result;
        } catch (BizException bizException) {
            if (bizException.getErrorCode() == BizErrorCode.VERIFY_CODE_COMPARE_MAX_TIMES) {
                verificationCodeDomain.verifyFail(verificationCodeDTO.getUniqueId());
            } else if (bizException.getErrorCode() == BizErrorCode.VERIFY_CODE_UNMATCHED) {
                verificationCodeDomain.incVerifyTimes(verificationCodeDTO.getUniqueId());
            }
            throw bizException;
        }
    }
}
