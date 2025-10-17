package com.odk.baseutil.enums;

import com.odk.base.enums.IEnum;

/**
 * InnerRoleEnum
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/26
 */
public enum InnerRoleEnum implements IEnum {

    ADMIN("ADMIN", "管理员"),

    NORMAL("MEMBER", "会员");


    /**
     * 管理员角色
     */
    public static final String ADMIN_ROLE = "ADMIN";

    private final String code;

    private final String description;

    InnerRoleEnum(String code, String description) {
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
