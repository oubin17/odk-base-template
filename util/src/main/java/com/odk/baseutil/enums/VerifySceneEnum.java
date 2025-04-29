package com.odk.baseutil.enums;

import com.odk.base.enums.IEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * VerifySceneEnum
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
public enum VerifySceneEnum implements IEnum {

    LOGIN("LOGIN", "登录"),

    REGISTER("REGISTER", "注册"),

    ;

    private final String code;

    private final String description;

    VerifySceneEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static VerifySceneEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (VerifySceneEnum sceneEnum : values()) {
            if (sceneEnum.code.equals(code)) {
                return sceneEnum;
            }
        }
        return null;
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
