package com.odk.baseutil.request.password;

import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.vo.request.BaseRequest;
import com.odk.baseutil.validate.EnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * PasswordUpdateRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/3/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PasswordUpdateRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = -8217657770336656448L;

    /**
     * 密码类型
     * {@link IdentificationTypeEnum}
     */
    @NotBlank(message = "identifyType不能为空")
    @EnumValue(enumClass = IdentificationTypeEnum.class, property = "code", message = "identifyType非法")
    private String identifyType = IdentificationTypeEnum.PASSWORD.getCode();

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldIdentifyValue;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newIdentifyValue;
}
