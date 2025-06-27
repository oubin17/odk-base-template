package com.odk.basedomain.domain;

import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.enums.VerificationCodeStatusEnum;

/**
 * VerificationCodeDomain
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/6/27
 */
public interface VerificationCodeDomain {

    /**
     * 保存验证码生成结果
     *
     * @param verificationCodeDTO
     * @param uniqueId
     * @param status
     */
    void saveVerificationCodeFlow(VerificationCodeDTO verificationCodeDTO, String uniqueId, VerificationCodeStatusEnum status);

    /**
     * 验证成功
     * @param uniqueId
     */
    void verifySuccess(String uniqueId);

    /**
     * 增加验证失败次数
     *
     * @param uniqueId
     */
    void incVerifyTimes(String uniqueId);
    /**
     * 验证失败
     * @param uniqueId
     */
    void verifyFail(String uniqueId);
}
