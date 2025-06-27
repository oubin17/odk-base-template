package com.odk.baseutil.enums;

import com.odk.base.enums.IEnum;

/**
 * VerificationCodeStatusEnum
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/6/27
 */
public enum VerificationCodeStatusEnum implements IEnum {

    GENERATE_FAIL("-1", "验证码生成失败"),

    GENERATE_SUCCESS("0", "验证码生成成功"),

    COMPARE_FAIL("1", "验证码验证失败"),

    COMPARE_SUCCESS("2", "验证码验证成功"),

    ;

    private final String code;

    private final String description;

    VerificationCodeStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
