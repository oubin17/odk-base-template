package com.odk.baseutil.request.password;

import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.vo.request.BaseRequest;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.validate.EnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * PasswordSetRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PasswordSetRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = -6958692769459169433L;

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
    @NotBlank(message = "密码不能为空")
    private String identifyValue;

    /**
     * 验证相关信息
     */
    private VerificationCodeDTO verificationCode;
}
