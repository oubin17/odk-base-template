package com.odk.baseutil.request;

import com.odk.base.enums.user.TokenTypeEnum;
import com.odk.base.vo.request.PageParamRequest;
import com.odk.baseutil.validate.EnumValue;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * UserListQueryRequest
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserListQueryRequest extends PageParamRequest {

    @Serial
    private static final long serialVersionUID = 1154096091924473754L;

    @Size(min = 1, max = 10, message = "userName长度必须在1-10之间")
    private String userName;

    @Size(min = 1, max = 20, message = "loginId长度必须在1-20之间")
    private String loginId;

    @EnumValue(enumClass = TokenTypeEnum.class, property = "code", message = "loginType非法")
    private String loginType = TokenTypeEnum.MOBILE.getCode();

}
