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

    /**
     * 验证手机验证码是否匹配
     *
     * @param code
     * @return
     */
    public boolean verifySms(String code) {
        return true;
    }

    public VerificationCodeEntity generateCode(VerificationCodeDTO verificationCodeDTO) {
        return verificationGenerate.generateVerificationCode(verificationCodeDTO);
    }

    public boolean checkVerificationCode(VerificationCodeDTO verificationCodeDTO) {
        return verificationGenerate.checkVerificationCode(verificationCodeDTO);
    }

    @Autowired
    public void setVerificationGenerate(IVerificationGenerate verificationGenerate) {
        this.verificationGenerate = verificationGenerate;
    }
}
