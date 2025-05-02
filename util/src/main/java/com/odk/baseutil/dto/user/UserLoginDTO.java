package com.odk.baseutil.dto.user;

import com.odk.base.dto.DTO;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserLoginDTO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/1/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginDTO extends DTO {

    /**
     * 登录ID
     */
    private String loginId;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 密码类型
     */
    private String identifyType;

    /**
     * 密码
     */
    private String identifyValue;

    /**
     * 验证相关信息
     */
    private VerificationCodeDTO verificationCode;

}
