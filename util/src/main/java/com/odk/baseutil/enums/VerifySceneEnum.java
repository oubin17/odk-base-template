package com.odk.baseutil.enums;

import com.odk.base.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * VerifySceneEnum
 *
 * 最好是要做成配置化，配置在配置文件中
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
@Getter
public enum VerifySceneEnum implements IEnum {

    LOGIN("LOGIN", 120, 3, 5, "登录"),

    REGISTER("REGISTER", 180, 3, 10, "注册"),

    ;

    private final String code;

    /**
     * 验证码过期时间
     */
    private final int expireTime;

    /**
     * 单个验证码最大验证次数
     */
    private final int maxVerifyTimes;

    /**
     * 单个验证码24小时内最大发送次数
     */
    private final int maxSendPerDay;

    private final String description;

    VerifySceneEnum(String code, int expireTime, int maxVerifyTimes, int maxSendPerDay, String description) {
        this.code = code;
        this.expireTime = expireTime;
        this.maxVerifyTimes = maxVerifyTimes;
        this.maxSendPerDay = maxSendPerDay;
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
