package com.odk.basedomain.domain.impl;

import com.odk.basedomain.model.verificationcode.VerificationCodeDO;
import com.odk.basedomain.domain.VerificationCodeDomain;
import com.odk.basedomain.repository.verificationcode.VerificationCodeRepository;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.enums.VerificationCodeStatusEnum;
import com.odk.baseutil.userinfo.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * VerificationCodeDomainImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/6/27
 */
@Service
public class VerificationCodeDomainImpl implements VerificationCodeDomain {

    private VerificationCodeRepository verificationCodeRepository;

    @Override
    public void saveVerificationCodeFlow(VerificationCodeDTO verificationCodeDTO, String uniqueId, VerificationCodeStatusEnum status) {
        VerificationCodeDO codeDO = new VerificationCodeDO();
        codeDO.setVerifyKey(verificationCodeDTO.getVerifyKey());
        codeDO.setVerifyType(verificationCodeDTO.getVerifyType());
        codeDO.setVerifyScene(verificationCodeDTO.getVerifyScene().getCode());
        codeDO.setVerifyTimes(0);
        codeDO.setStatus(status.getCode());
        codeDO.setUserId(SessionContext.getLoginIdOrDefault(null));
        codeDO.setUniqueId(uniqueId);
        verificationCodeRepository.save(codeDO);
    }

    @Override
    public void verifySuccess(String uniqueId) {
        verificationCodeRepository.updateStatusByUniqueId(uniqueId, VerificationCodeStatusEnum.COMPARE_SUCCESS.getCode(), SessionContext.getLoginIdOrDefault(null), LocalDateTime.now());
    }

    @Override
    public void incVerifyTimes(String uniqueId) {
        verificationCodeRepository.updateVerifyTimes(uniqueId, SessionContext.getLoginIdOrDefault(null), LocalDateTime.now());
    }

    @Override
    public void verifyFail(String uniqueId) {
        verificationCodeRepository.updateStatusByUniqueId(uniqueId, VerificationCodeStatusEnum.COMPARE_FAIL.getCode(), SessionContext.getLoginIdOrDefault(null), LocalDateTime.now());
    }

    @Autowired
    public void setVerificationCodeRepository(VerificationCodeRepository verificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
    }

}
