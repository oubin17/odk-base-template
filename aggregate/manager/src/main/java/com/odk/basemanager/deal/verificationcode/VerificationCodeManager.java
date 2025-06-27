package com.odk.basemanager.deal.verificationcode;

import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.basedomain.domain.VerificationCodeDomain;
import com.odk.baseinfra.verificationcode.IVerificationGenerate;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.VerificationCodeEntity;
import com.odk.baseutil.enums.VerificationCodeStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class VerificationCodeManager {

    private IVerificationGenerate verificationGenerate;

    private VerificationCodeDomain verificationCodeDomain;

    public VerificationCodeEntity generate(VerificationCodeDTO verificationCodeDTO) {
        VerificationCodeEntity generate = verificationGenerate.generate(verificationCodeDTO);
        if (null == generate) {
            verificationCodeDomain.saveVerificationCodeFlow(verificationCodeDTO, null, VerificationCodeStatusEnum.GENERATE_FAIL);
        } else {
            verificationCodeDomain.saveVerificationCodeFlow(verificationCodeDTO, generate.getUniqueId(), VerificationCodeStatusEnum.GENERATE_SUCCESS);
        }
        return generate;
    }

    /**
     *
     *
     * @param verificationCodeDTO
     * @return
     */
    @Deprecated
    public boolean compare(VerificationCodeDTO verificationCodeDTO) {
        return verificationGenerate.compare(verificationCodeDTO);
    }

    /**
     * 对比并校验
     *
     * @param verificationCodeDTO
     * @return
     */
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

    @Autowired
    public void setVerificationGenerate(IVerificationGenerate verificationGenerate) {
        this.verificationGenerate = verificationGenerate;
    }

    @Autowired
    public void setVerificationCodeDomain(VerificationCodeDomain verificationCodeDomain) {
        this.verificationCodeDomain = verificationCodeDomain;
    }
}
