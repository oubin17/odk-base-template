package com.odk.baseutil.request;

import com.odk.base.enums.user.TokenTypeEnum;
import com.odk.base.vo.request.BaseRequest;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.baseutil.validate.EnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * VerificationCodeRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VerificationCodeRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = 3753890578092494681L;

    /**
     * 登录 ID：手机号、邮箱、用户名、第三方登录唯一标识
     */
    @NotBlank(message = "verifyKey不能为空")
    private String verifyKey;

    /**
     * 验证码类型：手机、邮箱、用户名、第三方登录唯一标识
     */
    @NotBlank(message = "verifyType不能为空")
    @EnumValue(enumClass = TokenTypeEnum.class, property = "code", message = "loginType非法")
    private String verifyType;

    /**
     * 验证码场景：登录、注册、找回密码等
     */
    @NotBlank(message = "verifyScene不能为空")
    @EnumValue(enumClass = VerifySceneEnum.class, property = "code", message = "verifyScene非法")
    private String verifyScene;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 唯一标识
     */
    private String uniqueId;
}
