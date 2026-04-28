package com.odk.baseutil.enums;

import com.odk.base.enums.IEnum;
import lombok.Getter;

/**
 * VerifyTypeEnum
 *
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/27
 */
@Getter
public enum VerifyTypeEnum implements IEnum {

    /**
     * 未登录时
     * 1-手机号
     * 2-邮箱
     * 登录时
     * 3-用户 id
     */
    USER_ID("0", "USER_ID"),

    MOBILE("1", "MOBILE"),

    EMAIL("2", "EMAIL");

    private final String code;

    private final String description;


    VerifyTypeEnum(String code, String description) {
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
