package com.odk.baseapi.request;

import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.enums.user.TokenTypeEnum;
import com.odk.base.vo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * UserRegisterRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisterRequest extends BaseRequest {
    @Serial
    private static final long serialVersionUID = -5394053093156366659L;

    /**
     * 登录ID
     */
    private String loginId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 登录类型
     * {@link TokenTypeEnum}
     */
    private String loginType = TokenTypeEnum.MOBILE.getCode();

    /**
     * 密码类型
     * {@link IdentificationTypeEnum}
     */
    private String identifyType = IdentificationTypeEnum.PASSWORD.getCode();

    /**
     * 密码
     */
    private String identifyValue;

    /**
     * 验证码
     */
    private String verificationCode;

}
