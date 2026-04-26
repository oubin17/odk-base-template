package com.odk.baseinfra.verificationcode.entity;

import lombok.Data;

/**
 * VerifyCodeEntity
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/26
 */
@Data
public class VerifyCodeEntity {

    /**
     * 验证码
     */
    private String code;

    /**
     * 过期时间
     */
    private String min;
}
