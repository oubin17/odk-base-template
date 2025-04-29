package com.odk.baseinfra.verificationcode;

import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.VerificationCodeEntity;

/**
 * IVerificationGenerate
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
public interface IVerificationGenerate {

    /**
     * 生成验证码
     * @param verificationCodeDTO
     * @return
     */
    VerificationCodeEntity generateVerificationCode(VerificationCodeDTO verificationCodeDTO);

    /**
     * 校验验证码
     *
     * @param verificationCodeDTO
     * @return
     */
    boolean checkVerificationCode(VerificationCodeDTO verificationCodeDTO);
}
