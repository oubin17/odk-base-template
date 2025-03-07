package com.odk.baseutil.request.password;

import com.odk.base.enums.user.IdentificationTypeEnum;
import com.odk.base.vo.request.BaseRequest;
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
    private String identifyType = IdentificationTypeEnum.PASSWORD.getCode();

    /**
     * 旧密码
     */
    private String oldIdentifyValue;

    /**
     * 新密码
     */
    private String newIdentifyValue;
}
