package com.odk.baseutil.request;

import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.enums.user.TokenTypeEnum;
import com.odk.base.vo.request.BaseRequest;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.validate.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * UserLoginRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserLoginRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = -5548611718928196243L;

    /**
     * 登录ID
     */
    @NotBlank(message = "loginId不能为空")
    @Size(min = 1, max = 20, message = "loginId长度必须在1-20之间")
    private String loginId;

    /**
     * 登录类型
     * {@link TokenTypeEnum}
     */
    @NotBlank(message = "loginType不能为空")
    @EnumValue(enumClass = TokenTypeEnum.class, property = "code", message = "loginType非法")
    private String loginType = TokenTypeEnum.MOBILE.getCode();

    /**
     * 密码类型
     * {@link IdentificationTypeEnum}
     */
    @NotBlank(message = "identifyType不能为空")
    @EnumValue(enumClass = IdentificationTypeEnum.class, property = "code", message = "identifyType非法")
    private String identifyType = IdentificationTypeEnum.PASSWORD.getCode();

    /**
     * 密码
     */
    private String identifyValue;

    /**
     * 验证相关信息
     */
    private VerificationCodeDTO verificationCode;
}
