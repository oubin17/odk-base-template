package com.odk.baseutil.dto.verificationcode;

import com.odk.base.dto.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * VerificationCodeDTO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VerificationCodeDTO extends DTO {

    /**
     * 登录 ID：手机号、邮箱、用户名、第三方登录唯一标识
     */
    private String verifyKey;

    /**
     * 验证码类型：手机、邮箱、用户名、第三方登录唯一标识
     */
    private String verifyType;

    /**
     * 验证码场景：登录、注册、找回密码等
     */
    private String verifyScene;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 验证码唯一 ID
     */
    private String uniqueId;
}
