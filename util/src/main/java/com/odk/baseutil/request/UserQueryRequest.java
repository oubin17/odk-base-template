package com.odk.baseutil.request;

import com.odk.base.enums.user.TokenTypeEnum;
import com.odk.base.vo.request.BaseRequest;
import com.odk.baseutil.validate.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * UserQueryRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryRequest extends BaseRequest {

    @Serial
    private static final long serialVersionUID = -8631757065773576507L;

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

}
