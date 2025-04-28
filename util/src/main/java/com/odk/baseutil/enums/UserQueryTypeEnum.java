package com.odk.baseutil.enums;

import com.odk.base.enums.IEnum;

/**
 * UserQueryTypeEnum
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/28
 */
public enum UserQueryTypeEnum implements IEnum {

    USER_ID("USER_ID", "用户ID"),
    SESSION("SESSION", "会话"),
    LOGIN_ID("LOGIN_ID", "登录ID"),

    USER_ID_LIST("USER_ID_LIST", "用户ID列表"),
    ;

    private final String code;

    private final String description;


    UserQueryTypeEnum(String code, String description) {
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
