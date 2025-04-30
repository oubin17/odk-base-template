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
     * 生成验证码:简单实现了验证码生成逻辑，实际开发中需要补充额外工作
     * 1. 对接短信服务、邮件服务；
     * 2. 数据库记录每次生成的明细；
     *
     * @param verificationCodeDTO
     * @return
     */
    VerificationCodeEntity generate(VerificationCodeDTO verificationCodeDTO);

    /**
     * 校验验证码：只比较是否匹配，不做删除操作或增加错误次数
     *
     * @param verificationCodeDTO
     * @return
     */
    boolean compare(VerificationCodeDTO verificationCodeDTO);

    /**
     * 校验验证码:如果不通过，则根据策略增加错误次数或删除验证码
     *
     * @param verificationCodeDTO
     * @return
     */
    boolean compareAndIncr(VerificationCodeDTO verificationCodeDTO);
}
