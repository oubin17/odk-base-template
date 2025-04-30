package com.odk.basemanager.deal.verificationcode;

import com.odk.baseinfra.verificationcode.IVerificationGenerate;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.VerificationCodeEntity;
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

    public VerificationCodeEntity generate(VerificationCodeDTO verificationCodeDTO) {
        return verificationGenerate.generate(verificationCodeDTO);
    }

    /**
     * 只对比
     *
     * @param verificationCodeDTO
     * @return
     */
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
        return verificationGenerate.compareAndIncr(verificationCodeDTO);
    }

    @Autowired
    public void setVerificationGenerate(IVerificationGenerate verificationGenerate) {
        this.verificationGenerate = verificationGenerate;
    }
}
